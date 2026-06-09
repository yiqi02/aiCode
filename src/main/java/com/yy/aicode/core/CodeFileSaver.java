package com.yy.aicode.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.yy.aicode.ai.model.HtmlCodeResult;
import com.yy.aicode.ai.model.MultiFileCodeResult;
import com.yy.aicode.model.entity.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 文件保存器
 */
@Deprecated
public class CodeFileSaver {

    /**
     * 文件保存的根目录
     */
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 保存 HTML 网页代码
     *
     * @param htmlCodeResult
     * @return
     */
    public static File saveHtmlCodeResult(HtmlCodeResult htmlCodeResult) {
        //构造基础路径
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        //调用方法保存单个文件
        writeToFile(baseDirPath,"index.html", htmlCodeResult.getHtmlCode());
        return new File(baseDirPath);
    }

    /**
     * 保存多文件网页代码
     *
     * @param result
     * @return
     */
    public static File saveMultiFileCodeResult(MultiFileCodeResult result) {
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        writeToFile(baseDirPath, "style.css", result.getCssCode());
        writeToFile(baseDirPath, "script.js", result.getJsCode());
        return new File(baseDirPath);
    }

    /**
     * 构建文件的唯一路径（业务路径——雪花id）：tmp/code_output/bizType_雪花 ID
     *
     * @param bizType 代码生成类型
     * @return
     */
    private static String buildUniqueDir(String bizType) {
        //hutu工具类 格式化
        String uniqueDirName = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 保存单个文件
     *
     * @param dirPath
     * @param filename
     * @param content
     */
    private static void writeToFile(String dirPath, String filename, String content) {
        String filePath = dirPath + File.separator + filename;
        //hutu工具类
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
    }
}
