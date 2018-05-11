<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="text-center">
    <ul class="pagination">
        <c:choose>
            <c:when test="${paginator.total lt 1}">
                <li class="disabled"><span class="text-muted">无记录</span></li>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${paginator.total eq 1}">
                        <li class="disabled"><a>&laquo;</a></li>
                        <li class="disabled"><a>1</a></li>
                        <li class="disabled"><a>&raquo;</a></li>
                        <li class="disabled"><span class="text-muted">共${paginator.total}条记录</span></li>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${paginator.hasPreviousPage}">
                                <li><a href="javascript:void(0);" onclick="pagination(1);" title="First">&laquo;</a></li>
                                <li><a href="javascript:void(0);" onclick="pagination(${paginator.prePage});" title="Previous">&lsaquo;</a></li>
                            </c:when>
                            <c:otherwise>
                                <li class="disabled"><a>&laquo;</a></li>
                                <li class="disabled"><a>&lsaquo;</a></li>
                            </c:otherwise>
                        </c:choose>
                        <c:forEach items="${paginator.navigatepageNums}" var="number">
                            <c:choose>
                                <c:when test="${number eq paginator.pageNum}">
                                    <li class="disabled"><a href="javascript:void(0);"><b>${number}</b></a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a href="javascript:void(0);" onclick="pagination(${number});">${number}</a></li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${paginator.hasNextPage}">
                                <li><a href="javascript:void(0);" onclick="pagination(${paginator.nextPage});" title="Next">&rsaquo;</a></li>
                                <li><a href="javascript:void(0);" onclick="pagination(${paginator.pages});" title="Last">&raquo;</a></li>
                            </c:when>
                            <c:otherwise>
                                <li class="disabled"><a>&rsaquo;</a></li>
                                <li class="disabled"><a>&raquo;</a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
<script type="text/javascript">
    function pagination(page) {
        $('#page').val(page);
        $('#dataform').submit();
    }
</script>