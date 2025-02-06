 package com.tcs.hr;

import java.math.BigDecimal;
import java.sql.Date;

import java.sql.Timestamp;

public class AttendanceRecord {
    private String username;
    private Timestamp signInTime;
    private Timestamp signOutTime;
    private String location;
    private String latitude;
    private String longitude;
    private String empId;
    private String signOutLocation;
    private String leaveType;
    private Timestamp startDate;
    private Timestamp endDate;
    private String requester;
    private String status;
    private int id;
    private Date startdate;
    private Date enddate;
    private String leavePaymentType;
    private int userid;
    private int daysTaken;
    private String role;
    private String password;
    private String name;
    private String department;
    private String gender;
    private Date dob;
    private String email;
    private int mobile;
    private String nationality;
    private String blood;
    private String bankname;
    private String bankaccountno;
    private String ifsc;
    private Date joiningdate;
    private String fname;
    private String relation;
    private Date fdob;
    private String fgender;
    private int age;
    private String fblood;
    private String fnationality;
    private String profession;
    private int experience;
    private BigDecimal monthly;
    private String designation;
    private String taskName;
    private String taskDescription;
    private String assignedTo;
    private String taskStatus;
    private String priority;
    private String taskUsername;
    private String companyname;
    private String companyaddress;
    private String client;
    private String clientdesignation;
    private String clientmobile;
    private String clientemail;
    private String work;
    private int clientamount;
    private String meetingIn;
    private String meetingOut;
    private String salestaskstatus;
    private int taskid;
    private Date saledate;
    private double basicSalary;
    private double da;
    private double ta;
    private double hra;
    private double ma;
    private String slipstatus;
    private Timestamp requestDate;
    private Timestamp actionedDate;
    private String remarks;
    private String slipMonth;
    
    
    
	public String getSlipMonth() {
		return slipMonth;
	}
	public void setSlipMonth(String slipMonth) {
		this.slipMonth = slipMonth;
	}
	public Timestamp getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}
	public Timestamp getActionedDate() {
		return actionedDate;
	}
	public void setActionedDate(Timestamp actionedDate) {
		this.actionedDate = actionedDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSlipstatus() {
		return slipstatus;
	}
	public void setSlipstatus(String slipstatus) {
		this.slipstatus = slipstatus;
	}
	public double getBasicSalary() {
		return basicSalary;
	}
	public void setBasicSalary(double basicSalary) {
		this.basicSalary = basicSalary;
	}
	public double getDa() {
		return da;
	}
	public void setDa(double da) {
		this.da = da;
	}
	public double getTa() {
		return ta;
	}
	public void setTa(double ta) {
		this.ta = ta;
	}
	public double getHra() {
		return hra;
	}
	public void setHra(double hra) {
		this.hra = hra;
	}
	public double getMa() {
		return ma;
	}
	public void setMa(double ma) {
		this.ma = ma;
	}
	public Date getSaledate() {
		return saledate;
	}
	public void setSaledate(Date saledate) {
		this.saledate = saledate;
	}
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public String getSalestaskstatus() {
		return salestaskstatus;
	}
	public void setSalestaskstatus(String salestaskstatus) {
		this.salestaskstatus = salestaskstatus;
	}
	public String getClientmobile() {
		return clientmobile;
	}
	public void setClientmobile(String clientmobile) {
		this.clientmobile = clientmobile;
	}
	public String getMeetingIn() {
		return meetingIn;
	}
	public void setMeetingIn(String meetingIn) {
		this.meetingIn = meetingIn;
	}
	public String getMeetingOut() {
		return meetingOut;
	}
	public void setMeetingOut(String meetingOut) {
		this.meetingOut = meetingOut;
	}
	public int getClientamount() {
		return clientamount;
	}
	public void setClientamount(int clientamount) {
		this.clientamount = clientamount;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getCompanyaddress() {
		return companyaddress;
	}
	public void setCompanyaddress(String companyaddress) {
		this.companyaddress = companyaddress;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getClientdesignation() {
		return clientdesignation;
	}
	public void setClientdesignation(String clientdesignation) {
		this.clientdesignation = clientdesignation;
	}
	public String getClientemail() {
		return clientemail;
	}
	public void setClientemail(String clientemail) {
		this.clientemail = clientemail;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getTaskUsername() {
		return taskUsername;
	}
	public void setTaskUsername(String taskUsername) {
		this.taskUsername = taskUsername;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public BigDecimal getMonthly() {
		return monthly;
	}
	public void setMonthly(BigDecimal monthly) {
		this.monthly = monthly;
	}
	public int getExperience() {
		return experience;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	public String getFgender() {
		return fgender;
	}
	public void setFgender(String fgender) {
		this.fgender = fgender;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public Date getFdob() {
		return fdob;
	}
	public void setFdob(Date fdob) {
		this.fdob = fdob;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getFblood() {
		return fblood;
	}
	public void setFblood(String fblood) {
		this.fblood = fblood;
	}
	public String getFnationality() {
		return fnationality;
	}
	public void setFnationality(String fnationality) {
		this.fnationality = fnationality;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public Date getJoiningdate() {
		return joiningdate;
	}
	public void setJoiningdate(Date joiningdate) {
		this.joiningdate = joiningdate;
	}
	public String getIfsc() {
		return ifsc;
	}
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getBankaccountno() {
		return bankaccountno;
	}
	public void setBankaccountno(String bankaccountno) {
		this.bankaccountno = bankaccountno;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date date) {
		this.dob = date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getMobile() {
		return mobile;
	}
	public void setMobile(int mobile) {
		this.mobile = mobile;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getBlood() {
		return blood;
	}
	public void setBlood(String blood) {
		this.blood = blood;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getDaysTaken() {
		return daysTaken;
	}
	public void setDaysTaken(int daysTaken) {
		this.daysTaken = daysTaken;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Timestamp getSignInTime() {
		return signInTime;
	}
	public void setSignInTime(Timestamp signInTime) {
		this.signInTime = signInTime;
	}
	public Timestamp getSignOutTime() {
		return signOutTime;
	}
	public void setSignOutTime(Timestamp signOutTime) {
		this.signOutTime = signOutTime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getSignOutLocation() {
		return signOutLocation;
	}
	public void setSignOutLocation(String signOutLocation) {
		this.signOutLocation = signOutLocation;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getRequester() {
		return requester;
	}
	public void setRequester(String requester) {
		this.requester = requester;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	public String getLeavePaymentType() {
		return leavePaymentType;
	}
	public void setLeavePaymentType(String leavePaymentType) {
		this.leavePaymentType = leavePaymentType;
	}
    
}
