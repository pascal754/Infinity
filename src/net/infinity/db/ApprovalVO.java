package net.infinity.db;
import java.sql.Timestamp;

public class ApprovalVO {
	private String docNo;
	private int type;
	private int teamCode;
	private int approver;
	private int approvalOrder;
	private int approved;
	private Timestamp approvedTime;
	private String comment;
	public String getDocNo() {
		return docNo;
	}
	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(int teamCode) {
		this.teamCode = teamCode;
	}
	public int getApprover() {
		return approver;
	}
	public void setApprover(int approver) {
		this.approver = approver;
	}
	public int getApprovalOrder() {
		return approvalOrder;
	}
	public void setApprovalOrder(int approvalOrder) {
		this.approvalOrder = approvalOrder;
	}
	public int getApproved() {
		return approved;
	}
	public void setApproved(int approved) {
		this.approved = approved;
	}
	public Timestamp getApprovedTime() {
		return approvedTime;
	}
	public void setApprovedTime(Timestamp approvedTime) {
		this.approvedTime = approvedTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
