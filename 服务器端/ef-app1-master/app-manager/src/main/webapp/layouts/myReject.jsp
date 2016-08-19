<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="am-modal am-modal-prompt" tabindex="-1" id="my-reject">
    <div class="am-modal-dialog">
        <%--<div class="am-modal-hd">驳回意见</div>--%>
        <div class="am-modal-bd">
            请填写驳回意见
            <textarea id="message" maxlength="100" placeholder="此项为必填项" class="am-modal-prompt-input" required="required"></textarea>
        </div>
        <div class="am-modal-footer">
            <span class="am-modal-btn" data-am-modal-cancel>取消</span>
            <span class="am-modal-btn" data-am-modal-confirm>提交</span>
        </div>
    </div>
</div>