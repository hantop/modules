<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<link rel="stylesheet" href="/static/component/bootstrap/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>

<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>

<div class="container  content-top">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/system/index">微信管理</a></li>
                <li class="active"><b>菜单管理</b></li>
            </ol>
            <hr/>
            <div class="col-md-4">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <ul id="tree" class="ztree" style="width:230px; overflow:auto;"></ul>
                    </div>
                </div>
            </div>
            <div class="col-md-8">
                <div class="panel panel-default panel-form hide">
                    <div class="panel-body">
                        <form class="form-horizontal weixin-menu-form" role="form">
                            <div class="form-group has-feedback">
                                <label class="col-sm-2 control-label text-right">
                                    菜单名称：
                                </label>

                                <div class="col-sm-6">
                                    <input class="form-control input-sm" placeholder="菜单名称" readonly name="name"/>
                                </div>
                            </div>
                            <div class="form-group has-feedback">
                                <label class="col-sm-2 control-label text-right">
                                    排序：
                                </label>

                                <div class="col-sm-6">
                                    <input class="form-control input-sm" placeholder="排序" name="sort" type="number"/>
                                </div>
                            </div>
                            <div class="form-group has-feedback no-edit weixin-menu hide">
                                <label class="col-sm-2 control-label text-right">
                                    菜单内容：
                                </label>

                                <div class="col-sm-6">
                                    <label class="radio-inline radio-tab active" data-target="send-text">
                                        <input type="radio" value="click" name="sex" checked>发送消息
                                    </label>

                                    <label class="radio-inline radio-tab"
                                           data-target="redirect-url">
                                        <input type="radio" value="view" name="sex">跳转网页
                                    </label>
                                </div>
                            </div>
                            <div class="form-group has-feedback  no-edit weixin-menu hide">
                                <div class="tab-content col-sm-12">
                                    <div class="tab-pane fade in active col-sm-offset-1" id="send-text">
                                        <textarea name="content" class="form-control" rows="8" placeholder="请输入文字消息"
                                                  maxlength="600"></textarea>

                                        <p style="color: #8d8d8d;float: right;">还可以输入<em>600</em>字</p>
                                    </div>
                                    <div class="tab-pane fade" id="redirect-url">
                                        <input name="url" class="form-control" placeholder="页面地址"/>
                                    </div>
                                </div>
                            </div>
                            <shiro:hasPermission name="weixinmenu:edit">
                                <button type="button" class="btn btn-primary col-sm-offset-3 btn-save">保存并发布</button>
                            </shiro:hasPermission>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="application/javascript">
    $(function () {

        function getLength(str) {
            return str.replace(/[^\x00-\xff]/g, "aa").length;
        };

        //zTree配置
        var setting = {
            data: {
                key: {
                	//设置子节点的属性名称为sub_button
                    children: "sub_button",
                    //设置节点url的属性名称为remote
                    url: 'remote'
                }
            },
            view: {
            	//节点可以多选
                selectedMulti: false,
                //鼠标在节点上时显示自定义按钮的方法
                addHoverDom: addHoverDom,
                //鼠标离开节点时移除自定义按钮的方法
                removeHoverDom: removeHoverDom
            },
            callback: {
            	//修改节点名称时触发
                beforeRename: function (treeId, node, newName, isCancel) {
                    var length = getLength(newName);
                    var level = node.level;
                    //2级菜单字数
                    if (level == 2 && length > 30) {
                        bootbox.alert({
                            title: '提示',
                            size: 'small',
                            message: "二级菜单最多15个汉字,两个英文字符算一个汉字",
                        });
                        return false;
                    }
                    //1级菜单字数
                    else if (level == 1 && length > 10) {
                        bootbox.alert({
                            title: '提示',
                            size: 'small',
                            message: "一级菜单最多4个汉字,两个英文字符算一个汉字",
                        });
                        return false;
                    }
                    return true;
                },
                onClick: onClick,
                afterEditName: function (treeId, treeNode, newName) {
                    $('input[name="name"]').val(newName);
                }
            }
        };

        //点击节点时，将节点数据填到表单
        function onClick(event, treeId, treeNode) {
            var level = treeNode.level;
            //如果是根节点，不显示右边表单
            if (level > 0) {
                $('.panel-form').removeClass('hide');
            } else {
                $('.panel-form').addClass('hide');
            }
            //向元素附加节点数据
            $('form.weixin-menu-form').data('node', treeNode);
            //将被选节点的数据放进表单
            var name = treeNode.name;
            var type = treeNode.type;
            var sort = treeNode.sort;
            $('input[name="name"]').val(name);
            $('input[name="sort"]').val(sort);
            $('.weixin-menu').removeClass('hide');
            $('input[name="url"]').val(treeNode.url);
            $('textarea[name="content"]').val(treeNode.content);
            $('div.no-edit').removeClass('hide');
            if (treeNode.name === '我要出借' || treeNode.name === '绑定账户' || treeNode.name === '微信客服') {
                $('div.no-edit').addClass('hide');
                return;
            } 
            //如果节点是view类型，选中“跳转网页”
            else if (type === 'view') {
                $('label.radio-tab[data-target="redirect-url"] input').trigger('click');
            } 
            //如果节点是click类型，选中“发送消息”
            else if (type === 'click') {
                $('label.radio-tab[data-target="send-text"] input').trigger('click');
            } else if (type === "") {
                console.info("type:" + type);
            } else {
                $('.weixin-menu').addClass('hide')
                $('input[name="url"]').val('');
                $('textarea[name="content"]').val('');
            }
        }

        //鼠标在节点上时显示自定义按钮的方法
        function addHoverDom(treeId, treeNode) {
            <shiro:hasPermission name="weixinmenu:edit">
            if (treeNode.name === '我要出借' || treeNode.name === '绑定账户' || treeNode.name === '微信客服') return;
            // 如果删除或添加按钮已存在将不添加按钮动作
            if ($("#" + treeNode.tId + "_add").length > 0) return;
            if ($("#" + treeNode.tId + "_remove").length > 0) return;
            var node = $("#" + treeNode.tId + "_span");
            // 如果当前节点不是二级节点就可以显示添加按钮
            if (treeNode.level != 2) {
                var add = $("<span class='button add' onfocus='this.blur();'></sapn>").attr({
                    "id": treeNode.tId + "_add",
                    "title": treeNode.name
                });
                node.after(add);
                //添加按钮click事件
                $(add).on('click', function () {
                    if (treeNode.level == 0 && treeNode.sub_button && treeNode.sub_button.length >= 3) {
                        bootbox.alert({
                            title: '提示',
                            size: 'small',
                            message: "菜单最多包括3个一级菜单",
                        });
                    } else if (treeNode.level == 1 && treeNode.sub_button && treeNode.sub_button.length >= 5) {
                        bootbox.alert({
                            title: '提示',
                            size: 'small',
                            message: "一级菜单最多包含5个二级菜单",
                        });
                    } else {
                        treeObj.addNodes(treeNode, {
                            'name': '菜单名称',
                            'type': 'click',
                            'url': ''
                        });
                        delete treeNode.type;
                    }
                });
            }
            //如果当前按钮是顶级节点不进行下面的动作
            if (treeNode.level == 0) {
                return false;
            }
            // 编辑按钮
            var edit = $("<span class='button edit' onfocus='this.blur();'></sapn>").attr({
                "id": treeNode.tId + "_edit",
                "title": treeNode.name
            });
            node.parent().append(edit);
            //编辑按钮click事件
            $(edit).on('click', function () {
                treeObj.editName(treeNode);
            });
            if (treeNode.name !== '服务中心') {
                // 删除按钮
                if (treeNode.level <= 2) {
                    var remove = $("<span class='button remove' onfocus='this.blur();'></sapn>").attr({
                        "id": treeNode.tId + "_remove",
                        "title": treeNode.name
                    });

                    node.parent().append(remove);
                    //删除按钮click事件
                    $(remove).on('click', function () {
                        treeObj.removeNode(treeNode, true);
                    });
                    return true;
                }
            }
            </shiro:hasPermission>

            return false;
        }

        //鼠标离开节点时移除自定义按钮的方法
        function removeHoverDom(treeId, treeNode) {
            $("#" + treeNode.tId + "_add").remove();
            $("#" + treeNode.tId + "_edit").remove();
            $("#" + treeNode.tId + "_remove").remove();
        }

       var zNodes = ${menu};
        var treeObj = $.fn.zTree.init($("#tree"), setting, zNodes);

        /*ztree 移除节点事件*/
        $("#tree").on('ztree_remove', function (treeId, node) {
            $('.weixin-menu').addClass('hide');
        });

        // tab 切换
        $("label.radio-tab input").on('click', function (e) {
            var $this = $(this).closest('label');
            var $container = $this.closest('div');
            var selector = "#" + $this.attr('data-target');
            var $target = $(selector);


            if ($this.hasClass('active')) return;
            var $previous = $container.find('.active:last');
            activate($this, $container);
            activate($target, $target.parent(), function () {
                var $active = $target.parent().find('> .active');
                $active.removeClass('in');
                $target.addClass('in');
            });

            var node = $('form.weixin-menu-form').data('node');
            node.type = $(this).val();
        });

        function activate(element, container, callback) {
            var $active = container.find('> .active');
            $active.removeClass('active');
            element.addClass('active');
            callback && callback();
        }

        // textarea 文字提示
        $('textarea[maxlength]').on('keyup', function (e) {
            var length = $(this).attr('maxlength');
            var textLength = $(this).val().length;
            var node = $('form.weixin-menu-form').data('node');
            node.content = $(this).val();
            var gap = length - textLength;
            $(this).next().find('em').text(gap);
        });
        //监听url网址输入内容绑定到node上面
        $('input[name="url"]').on('keyup', function () {
            var node = $('form.weixin-menu-form').data('node');
            node.url = $(this).val();
        });

        //监听sort输入内容绑定到node上面
        $('input[name="sort"]').on('keyup', function () {
            var node = $('form.weixin-menu-form').data('node');
            node.sort = $(this).val();
        });

        //点击“保存并发布”按钮
        $('form.weixin-menu-form .btn-save').on('click', function () {
        	//取得所有节点
            var nodes = treeObj.getNodes()[0].sub_button;
            ajaxMenu({
                url: path + '/weixin/menu/publishTestMenu',
                nodes: nodes,
                successCallback: function (resp) {
                	//如果成功发布到测试环境
                    if (resp.errcode === 0) {
                        bootbox.setLocale("zh_CN");
                        bootbox.confirm({
                            title: '提示',
                            size: 'small',
                            message: "测试公众发送成功<br/>是否发送到正式环境",
                            callback: function (result) {
                            	//如果确认发布到正式环境
                                if (result) {
                                    ajaxMenu(
                                            {
                                                url: path + '/weixin/menu/publishProdMenu',
                                                nodes: nodes,
                                                successCallback: function (resp2) {
                                                    if (resp2.errcode === 0) {
                                                        bootbox.alert({
                                                            title: '提示',
                                                            size: 'small',
                                                            message: '正式公众发送成功',
                                                            callback: function () {
                                                                window.location.reload()
                                                            }
                                                        });
                                                    } else {
                                                        bootbox.alert({
                                                            title: '提示',
                                                            size: 'small',
                                                            message: resp2.errmsg
                                                        });
                                                    }
                                                }
                                            }
                                    );
                                } else {
                                    bootbox.alert({
                                        title: '提示',
                                        size: 'small',
                                        message: '已取消'
                                    });
                                }
                            }
                        })
                    } else {
                        bootbox.alert({
                            title: '提示',
                            size: 'small',
                            message:
                            resp.errmsg
                        });
                    }
                }
            })
            return false;
        });

        function haosSpecialChar(node) {
            var str = node.url;
            return str.indexOf('"') > 0 || str.indexOf("'") > 0;
        }

        function ajaxMenu(param) {
            var nodes = param.nodes;
            for (var i = 0; i < nodes.length; i++) {
                var node = nodes[i];
                if (node.sub_button.length > 0) {
                    for (var j = 0; j < node.sub_button.length; j++) {
                        var subnode = node.sub_button[j];
                        var type = subnode.type;
                        if (type === 'view') {
                            if (haosSpecialChar(subnode)) {
                                bootbox.alert({
                                    title: '提示',
                                    size: 'small',
                                    message: 'url 链接中不能包含特殊字符 " 或 \' '
                                });
                                return;
                            }
                        }
                    }
                } else {
                    var type = node.type;
                    if (type === 'view') {
                        if (haosSpecialChar(node)) {
                            bootbox.alert({
                                title: '提示',
                                size: 'small',
                                message: 'url 链接中不能包含特殊字符 " 或 \' '
                            });
                            return;
                        }
                    }
                }
            }
            <shiro:hasPermission name="weixinmenu:edit">
            $.ajax({
                method: "POST",
                url: param.url ? param.url : path + '/weixin/menu/publishTestMenu',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8',
                data: JSON.stringify(param.nodes),
                beforeSend: function () {
                    HoldOn.open({
                        theme: "sk-cube-grid",
                        message: '请稍等.....',
                        backgroundColor: "#000"
                    });
                }
            })
                    .done(function (resp) {
                        if (param.successCallback) {
                            param.successCallback && param.successCallback(resp);
                        } else {
                            if(resp && resp.code == -1) {
                                bootbox.alert({
                                    title: '提示',
                                    size: 'small',
                                    message: resp2.errmsg
                                });
                                return null;
                            }
                            bootbox.alert({
                                title: '提示',
                                size: 'small',
                                message: resp.errmsg,
                            });
                        }
                    })
                    .fail(function () {
                        window.location.reload();
                        bootbox.alert({
                            title: '提示',
                            size: 'small',
                            message: "用户会话过期，请重新登陆",
                        });
                    })
                    .always(function () {
                        HoldOn.close();
                    });
        }

        </shiro:hasPermission>
    });


</script>