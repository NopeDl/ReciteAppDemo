package service;

import jakarta.servlet.http.HttpServletRequest;
import pojo.vo.Message;

public interface FileService {
    String getASPdf(String base64String, int fileId);

    String rePdf(String url);

    String reAsText(String context, int fileId);

    Message UpLoad(HttpServletRequest request);
}
