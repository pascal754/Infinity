package net.infinity.db;

import java.util.Date;

public class RejectedDocumentVO extends DocumentVO {
	private Date rejectedDate;
	private String comment;
	private int approvalOrder;
	private int approver;
	
	public Date getRejectedDate() {
		return rejectedDate;
	}
	public void setRejectedDate(Date rejectedDate) {
		this.rejectedDate = rejectedDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getApprovalOrder() {
		return approvalOrder;
	}
	public void setApprovalOrder(int approverOrder) {
		this.approvalOrder = approverOrder;
	}
	public int getApprover() {
		return approver;
	}
	public void setApprover(int approver) {
		this.approver = approver;
	}
}
