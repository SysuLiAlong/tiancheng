package com.tiancheng.ms.controller.auth;

import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.util.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${file.path}")
    private String filePath;

    static private String datePattern = "yyyy-MM-dd-HH-mm-ss-";

    @PostMapping(value = "/image/upload")
    public String fileUpload(@RequestParam(value = "image") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.FAIL, "文件为空，请重试！");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String datePrefix = DateUtil.format(new Date(), datePattern);
        File dest = new File(filePath + datePrefix + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datePrefix + fileName;
    }

    //文件下载相关代码

    @GetMapping(value = "/image/download")
    public void downloadImage(@RequestParam("imageName") String imageName, HttpServletResponse response) throws UnsupportedEncodingException {
        String fileUrl = filePath + imageName;
        if (StringUtils.isEmpty(imageName)) {
            throw new BusinessException(ErrorCode.FAIL, "参数异常！");
        }
        File file = new File(fileUrl);
        String realImageName = imageName.substring(datePattern.length());
        if (!file.exists()) {
            throw new BusinessException(ErrorCode.FAIL, "下载文件【" + realImageName + "】不存在！");
        }

        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(realImageName, "UTF-8"));// 设置文件名
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);

            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.FAIL, "下载失败图片失败,原因：【" + e.getMessage() + "】！");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @PostMapping("/image/delete")
    public void delFile(@RequestParam("imageName") String imageName) {
        String imageUrl = filePath + imageName;
        File file = new File(imageUrl);
        if (!file.exists()) {//文件是否存在
            throw new BusinessException(ErrorCode.FAIL, "文件不存在！");
        }
        file.delete();
    }
}
