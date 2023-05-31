package com.dky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dky.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService  {
    ResponseResult uploadImg(MultipartFile img);
}
