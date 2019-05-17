
"use strict";
//# sourceURL=main.js

// DOM 加载完再执行
$(function() {
    var _pageSize; // 存储用于搜索
    // 根据用户名、页面索引、页面大小获取用户列表
    function getCatalogs(pageIndex, pageSize) {
        $.ajax({
            url: "/admincatalogs",
            contentType : 'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize
            },
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    }

    //分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getCatalogs(pageIndex, pageSize);
        _pageSize = pageSize;
    });


    $("#rightContainer").on("click",".catalogs-delete-admin", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        //alert("123dsad");
        $.ajax({
            url: "/admincatalogs/catalogs/" + $(this).attr("catalogid") ,
            type: 'DELETE',
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function(data){
                if (data.success) {
                    getCatalogs(0, _pageSize);
                } else {
                    toastr.error("不能删除本分类，请先删除带有该分类的博客！");
                }
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });
});