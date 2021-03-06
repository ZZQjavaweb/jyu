
package com.crm.web.system;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;

import com.crm.entity.Position;
import com.crm.entity.Roles;
import com.crm.entity.Users;
import com.crm.entity.UsersRoles;
import com.crm.entity.UsersRolesId;
import com.modules.commons.PageAction;
import com.opensymphony.xwork2.ActionContext;

import net.sf.json.JSONArray;

public class UsersAction extends PageAction {

	private static final long serialVersionUID = -4691069988388166433L;
	private Users tusers;
	private List usersList;
	private Long id;
	private List rolesList;
	private Roles roles;
	private List usersRolesList;
	private List usersRolesTemp;
	private String name;
	private String idcardno;
	private String workphone;
	private String fax;
	private String sex;
	private String email;
	private String mobile;
	private String remark;
	private String loginName;
	private String password;
	private String repassword;
	private String checkmenu[];
	private String area;
	private String state;
	private String address;
	private Date birthday; //生日
	private Date entryDate; //入职日期
	private String qq; //QQ
	private List<Position> positions;
	private Long positionId;

	public UsersAction() {
		roles = new Roles();
	}

	public String execute() throws Exception {
		Map session = (Map) ActionContext.getContext().getSession();
		String area = "-1";

		Users users = (Users) session.get("users");
		int isAdmin = (Integer) session.get("isAdmin");
		if (2 == isAdmin)
			area = users.getArea();

		super.pager = usersManagerDao.findUsers(pageSize, pageNo, "-1", area);
		usersList = super.pager.getResultList();

		return "success";
	}

	public String view() throws Exception {
		setUsersRolesList(usersManagerDao.getUsers(id));
		// if (usersRolesList.size() > 0)
		setTusers(usersManagerDao.getUsersById(id));
		return "view";
	}

	/**
	 * 管理员查看自己的信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String viewInfo() throws Exception {
		Map session = (Map) ActionContext.getContext().getSession();
		// 获取Enterprise，并遍历Enterprise是否为超级管理员角色。
		Users u = (Users) session.get("users");
		if (u != null && u.getId() != null) {
			id = u.getId();
			setUsersRolesList(usersManagerDao.getUsers(id));
			// if (usersRolesList.size() > 0)
			setTusers(usersManagerDao.getUsersById(id));
		}

		return "view_info";
	}

	/**
	 * 用户编辑自己的信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editInfo() throws Exception {
		setRolesList(rolesManagerDao.findAllBYState("1"));
		setUsersRolesList(usersManagerDao.getUsers(id));
		setTusers(usersManagerDao.getUsersById(id));
		return "edit_info";
	}

	@SuppressWarnings("unchecked")
	public String edit() throws Exception {
		setRolesList(rolesManagerDao.findAllBYState("1"));
		setUsersRolesList(usersManagerDao.getUsers(id));
		// if (usersRolesList.size() > 0)
		setTusers(usersManagerDao.getUsersById(id));
		this.positions = (List<Position>) positionService.retrieveAllPositions().getResult();
		return "edit";
	}

	/**
	 * 用户编辑自己的信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateInfo() throws Exception {
		Users u = usersManagerDao.getUsersById(id);

		u.setName(name);
		u.setAddress(address);
		u.setSex(sex);
		u.setWorkphone(workphone);
		u.setIdcardno(idcardno);
		u.setEmail(email);
		u.setMobile(mobile);
		u.setAddress(address);
		u.setBirthday(birthday);
		u.setEntryDate(entryDate);
		u.setQq(qq);
		u.setRemark(remark);

		usersManagerDao.updateUsers(u);

		return this.viewInfo();
	}

	public String update() throws Exception {
		Users user = usersManagerDao.getUsersById(id);
		user.setName(name);
		user.setState(state);
		
		Position position = positionService.findPositionById(positionId);
		user.setPositionId(position);
		user.setSex(sex);
		user.setName(name);
		user.setWorkphone(workphone);
		user.setIdcardno(idcardno);
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAddress(address);
		user.setBirthday(birthday);
		user.setEntryDate(entryDate);
		user.setQq(qq);
		user.setRemark(remark);
		//user.setArea(area);
		
		List<UsersRoles> usersRolesList = new ArrayList<UsersRoles>();
		UsersRoles ru = null;
		Roles rs = null;
		UsersRolesId urId = null;
		String as[] = checkmenu;
		int j = 0;
		if(as != null && as.length > 0)
			j = as.length;
		
		for (int i = 0; i < j; i++) {
			String temp = as[i];
			ru = new UsersRoles();
			rs = new Roles();
			urId = new UsersRolesId();
			
			rs = rolesManagerDao.getRoles(Long.valueOf(temp.toString()));
			ru.setRoles(rs);
			ru.setUsers(user);
			urId.setRoles(rs);
			urId.setUsers(user);
			ru.setId(urId);
			usersRolesList.add(ru);
		}
		
		usersManagerDao.updateUsersAndRoles(user, usersRolesList);

		checkmenu = null;
		return "do_success";
	}

	@SuppressWarnings("unchecked")
	public String add_page() throws Exception {
		setRolesList(rolesManagerDao.findAllBYState("1"));
		this.positions = (List<Position>) positionService.retrieveAllPositions().getResult();
		return "add_page";
	}

	public String add() throws Exception {
		tusers = new Users();
		String tmp = loginName.trim().toString();
		tusers.setLoginName(tmp);
		initUsers();
		tusers.setArea(area);
		tusers.setState(state);

		password = (new Md5PasswordEncoder()).encodePassword(password, loginName);
		tusers.setPassword(password);
		tusers.setId(null);

		List<UsersRoles> tempUsersRolesList = new ArrayList<UsersRoles>();
		UsersRoles ru = null;
		Roles rs = null;
		UsersRolesId urId = null;
		String as[] = checkmenu;
		int j = 0;
		if(as != null && as.length > 0)
			j = as.length;

		for (int i = 0; i < j; i++) {
			String temp = as[i];
			ru = new UsersRoles();
			rs = new Roles();
			urId = new UsersRolesId();
			
			rs = rolesManagerDao.getRoles(Long.valueOf(temp.toString()));
			ru.setRoles(rs);
			ru.setUsers(tusers);
			urId.setRoles(rs);
			urId.setUsers(tusers);
			ru.setId(urId);
			tempUsersRolesList.add(ru);
		}

		usersManagerDao.addUsers(tusers, tempUsersRolesList);
		
		checkmenu = null;
		return "do_success";
	}

	public String delete() throws Exception {
		long ids = id.longValue();
		usersManagerDao.deleteUsers(Long.valueOf(ids));
		// usersRolesManagerDao.deleteUsersRolesByUsersId(Long.valueOf(ids));
		return "do_success";
	}

	public void initUsers() {
		Position position = positionService.findPositionById(positionId);
		tusers.setPositionId(position);
		tusers.setIsDel("0");
		tusers.setSex(sex);
		tusers.setName(name);
		tusers.setWorkphone(workphone);
		tusers.setIdcardno(idcardno);
		tusers.setEmail(email);
		//tusers.setFax(fax);
		tusers.setMobile(mobile);
		tusers.setAddress(address);
		tusers.setBirthday(birthday);
		tusers.setEntryDate(entryDate);
		tusers.setQq(qq);
		tusers.setRemark(remark);
	}

	public String checkName() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String name = request.getParameter("loginName");
		String orgName = request.getParameter("orgName");
		name = URLDecoder.decode(name, "UTF-8");
		orgName = URLDecoder.decode(orgName, "UTF-8");
		if (usersManagerDao.isNameUnique(name, orgName))
			return renderText("true");
		else
			return renderText("false");
	}
	
	/**
	 * ajax 模糊搜索
	 * @return
	 */
	public String ajaxFindName(){
		List result = null;
		try {
			result = usersManagerDao.findUserByName(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray json = JSONArray.fromObject(result);
		return renderText(json == null ? null : json.toString());
	}

	public List getUsersList() {
		return usersList;
	}

	public void setUsersList(List usersList) {
		this.usersList = usersList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List getRolesList() {
		return rolesList;
	}

	public void setRolesList(List rolesList) {
		this.rolesList = rolesList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcardno() {
		return idcardno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}

	public String getWorkphone() {
		return workphone;
	}

	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Roles getRoles() {
		return roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

	public List getUsersRolesList() {
		return usersRolesList;
	}

	public void setUsersRolesList(List usersRolesList) {
		this.usersRolesList = usersRolesList;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public List getUsersRolesTemp() {
		return usersRolesTemp;
	}

	public void setUsersRolesTemp(List usersRolesTemp) {
		this.usersRolesTemp = usersRolesTemp;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Users getTusers() {
		return tusers;
	}

	public void setTusers(Users tusers) {
		this.tusers = tusers;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String[] getCheckmenu() {
		return checkmenu;
	}

	public void setCheckmenu(String checkmenu[]) {
		this.checkmenu = checkmenu;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}
	
	

}