<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<div class="col-md-12" id="recharge_container1">

</div>
<script type="application/javascript">
    $(function($) {
        $('#recharge_container1').parent().on('tab.show',function(e) {
            console.info('tab2');
        });
    });
</script>

