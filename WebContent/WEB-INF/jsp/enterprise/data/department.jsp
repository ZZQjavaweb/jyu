﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<%@ include file="/common/head-import.jsp"%>

	<div class="main-content">
		<div class="breadcrumbs" id="breadcrumbs">
			<script type="text/javascript">
				try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
			</script>

			<ul class="breadcrumb">
				<li>
					<i class="icon-home home-icon"></i>
					<a href="${ctx}/usersAction!center.htm">首页</a>
				</li>
				<li>
					<a href="departmentAction!retrieveAllDepartments.action">人事管理</a>
				</li>
				<li class="active">部门管理</li>
			</ul>
		</div><!-- .breadcrumb -->

		<div class="page-content">
			<div>
				<h3 class="row-fluid header smaller  blue" style="line-height: 39px;">
					<span class="span7">部门列表</span><!-- /span -->

					<span class="span5 pull-right">
						<label class="inline">
							<a class="btn btn-success btn-sm" href="departmentAction!newPage.action">
								<i class="icon-plus"></i>
								添加
							</a>
						</label>
					</span><!-- /span -->
				</h3>
			</div><!-- /.page-header -->
			<div class="row">
				<div class="col-xs-12">
					<!-- PAGE CONTENT BEGINS -->
					<form class="form-search" id="searchForm" name="searchForm" method="post" action="${ctx }/departmentAction!retrieveAllDepartments.action">
						<div class="row">
							<div class="col-xs-12 col-sm-8" style="margin-left: 33.6%;">
								<div class="input-group" style="width: 300px;float: right;margin-top: -5px;margin-bottom: 10px;">
									<input type="text" name="keyWord" id="keyWord" value="${keyWord }" class="form-control search-query" placeholder="输入关键字" />
									<span class="input-group-btn" id="searchBtn">
											<button type="button" class="btn btn-primary btn-sm">
												查询
											<i class="icon-search icon-on-right bigger-110"></i>
										</button>
									</span>
								</div>
							</div>
						</div>
					</form>
						<div class="widget-box">
							<div class="widget-header header-color-blue2"></div>
							<div class="widget-body">	
								<div class="table-responsive">
									<div class="dataTables_wrapper" role="grid">
									<table id="sample-table-1" class="table table-striped table-bordered table-hover dataTable">
										<thead>
											<tr>
												<th>序号</th>
												<th>部门编号</th>
												<th>部门名称</th>
												<th>操作</th>
											</tr>
										</thead>
			
										<tbody>
											<c:forEach var="bean" items="${pager.resultList}" varStatus="status">
												<tr>
													<td>${status.count}</td>
													<td>${bean.code}</td>
													<td>${bean.name}</td>
													<td>
														<div class="action-buttons">
															 <a class="blue" href="positionAction!retrieveAllPositions.action?departmentId=${bean.id}&token=${bean.token}" data-rel="tooltip" title="查看职位">
																 <i class="icon-search bigger-130"></i>
															</a> 
															<a class="green" title="编辑" data-rel="tooltip" href="departmentAction!retrieveDepartmentById.action?id=${bean.id}&token=${bean.token}">
																<i class="icon-pencil bigger-130"></i>
															</a>
															<a class="red"  title="删除" href="#" data-rel="tooltip" onclick="ajaxUpdateDel(${bean.id},this,'${ctx}/ajaxDepartmentAction!delDepartment.action');">
																<i class="icon-trash bigger-130"></i>
															</a>
															<input type="hidden" class="security-token" value="${bean.token}">
														</div>
													</td>
												</tr>
											</c:forEach>
			
										</tbody>
									</table>
									<c:if test="${not empty pager.resultList}">
									<div class="row">
										<s:form action="departmentAction!retrieveAllDepartments.action" 
											namespace="/" method="get" id="form1" name="form1"
												theme="simple">
												<input type="hidden" name="keyWord" value="${keyWord }" />
											<%@ include file="../../common/pager.jsp"%>
										</s:form>
									</div>
									<!-- 分页 -->
									</c:if>
									</div>
								</div><!-- /.table-responsive -->
							</div><!-- widget-body end -->
						</div><!-- widget-box end -->
					<!-- PAGE CONTENT ENDS -->
				</div><!-- /.col -->
			</div><!-- /.row -->
		</div><!-- /.page-content -->
		
		
		<%@ include file="/common/copyright.jsp"%>
	</div><!-- /.main-content -->

				

	<%@ include file="/common/bottom-import.jsp"%>
	
	<script type="text/javascript">
		jQuery(function($) {
			setCurrent();
			$('[data-rel=tooltip]').tooltip();
			
			$("#searchBtn").click(function(){
				 $("#searchForm").submit();  
			});
		});
		
		function setCurrent(){
			$("#parent_6").addClass("active open");
			$("#sub_61").addClass("active");
		}
	</script>
	
	</body>
</html>