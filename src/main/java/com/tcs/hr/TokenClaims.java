package com.tcs.hr;

public class TokenClaims {

	private String empId;
	private String username;
	
	
	public TokenClaims(String username, String empId) 
	{
        this.username = username;
        this.empId = empId;
    }
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}
