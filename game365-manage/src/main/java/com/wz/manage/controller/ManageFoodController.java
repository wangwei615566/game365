package com.wz.manage.controller;

import com.github.pagehelper.Page;
import com.wz.cashloan.core.common.context.Constant;
import com.wz.cashloan.core.common.context.Global;
import com.wz.cashloan.core.common.util.JsonUtil;
import com.wz.cashloan.core.common.util.RdPage;
import com.wz.cashloan.core.common.util.ServletUtils;
import com.wz.cashloan.core.model.Goods;
import com.wz.cashloan.core.service.GoodService;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@Scope("prototype")
public class ManageFoodController extends ManageBaseController {

    @Resource
    private GoodService goodsService;

    /**
     * 商品列表
     * @param searchParams 查询参数 json字符串
     * @param current 当前页
     * @param pageSize 每页页数
     * @throws Exception 异常
     */
    @RequestMapping(value = "/manage/food/search/list.htm")
    public void searchList(@RequestParam(value="searchParams",required=false) String searchParams,
                           @RequestParam(value = "current") int current,
                           @RequestParam(value = "pageSize") int pageSize) throws Exception {
        Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
        Page<Goods> page = goodsService.pageList(params, current, pageSize);
        Map<String,Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page.getResult());
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 新增、编辑商品
     * @throws Exception 异常
     */
    @RequestMapping(value = "/manage/food/search/insert.htm")
    public void searchList(@RequestParam(value="id",required = false) Long id,
                           @RequestParam(value="name") String name,
                           @RequestParam(value="price") BigDecimal price,
                           @RequestParam(value="spec") String spec,
                           @RequestParam(value="picture") String picture,
                           @RequestParam(value="desc") String desc,
                           @RequestParam(value="state") Byte state) throws Exception {
        Map<String,Object> result = new HashMap<>();
        Goods goods = new Goods(name,price,spec,picture,state,desc,new Date(),new Date());
        int i = 0;
        if(id != null){
            goods.setId(id);
            i = goodsService.updateByPrimaryKeySelective(goods);
        } else {

            i = goodsService.insertGoods(goods);
        }
        if(i == 1){
            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "成功");
        } else {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "失败");
        }
        ServletUtils.writeToResponse(response, result);
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 删除商品
     * @throws Exception 异常
     */
    @RequestMapping(value = "/manage/food/search/delete.htm")
    public void searchList(@RequestParam(value="id",required = true) Long id) throws Exception {
        Map<String,Object> result = new HashMap<>();
        int i = goodsService.deleteById(id);
        if(i == 1){
            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "成功");
        } else {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "失败");
        }
        ServletUtils.writeToResponse(response, result);
    }

    /**
     * 上传图片
     * @throws Exception 异常
     */
    @RequestMapping(value = "/manage/food/search/upload.htm")
    public void upLoad(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String path = Global.getValue("upload_url");
        Map<String, Object> result = new HashMap<>();
        // 获得文件单个上传文件：
        MultipartFile file = null;
        // 获取文件名列表
        Iterator<String> it = ((MultipartRequest) request).getFileNames();
        // 循环遍历列表
        String fileUrl = null;
        String ext = null;
        while (it.hasNext()) {
            // 提取文件名
            String fileName = it.next();
            // 获取文件
            file = ((MultipartRequest) request).getFile(fileName);
            // 取文件的扩展名
            String fName = file.getOriginalFilename();
            // 取文件的类型
            ext = fName.substring(fName.lastIndexOf(".") + 1);

            CommonsMultipartFile cf = (CommonsMultipartFile) file;
            DiskFileItem fi = (DiskFileItem) cf.getFileItem();
            File f = fi.getStoreLocation();
            if (!f.exists()) {
                //先得到文件的上级目录，并创建上级目录，在创建文件
                f.getParentFile().mkdir();
                try {
                    //创建文件
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String serverHost = Global.getValue("server_host");
            fileUrl = uploadFile(path, ext, file);
            fileUrl=(fileUrl != null ? serverHost + fileUrl : "");
        }
        if (null == fileUrl || fileUrl.length()==0) {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, "文件上传失败");
            ServletUtils.writeToResponse(response, result);
            return;
        }
        result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
        result.put(Constant.RESPONSE_CODE_MSG, "文件上传成功");
        result.put("picture", fileUrl);
        ServletUtils.writeToResponse(response, result);
    }

    public String uploadFile(String path, String fileName, MultipartFile f) {
        //文件上传
        String str = System.currentTimeMillis() + "";
        String image = path + str + "." + fileName;
        File file2 = new File(image);
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(f.getInputStream());
            out = new BufferedOutputStream(new FileOutputStream(file2));
            try {
                byte[] buffer = new byte[1 * 1024];
                while (in.read(buffer) > 0) {
                    out.write(buffer);
                }
                out.flush();
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return image;
    }


}
