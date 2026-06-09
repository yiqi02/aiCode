package com.yy.aicode.core.saver;

import com.yy.aicode.ai.model.HtmlCodeResult;
import com.yy.aicode.exception.BusinessException;
import com.yy.aicode.exception.ErrorCode;
import com.yy.aicode.model.entity.enums.CodeGenTypeEnum;

public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath,"index.html",result.getHtmlCode());

    }

    //可以自己写校验代码

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        if(result.getHtmlCode()==null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"HTML参数不能为空");
        }
    }
}
