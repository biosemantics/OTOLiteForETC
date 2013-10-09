package edu.arizona.sirls.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.arizona.sirls.shared.beans.orders.OrderSet;

@RemoteServiceRelativePath("orders")
public interface OrderService extends RemoteService {
	ArrayList<OrderSet> getOrderSets(String uploadID) throws Exception; 
	
	void saveOrderSet(OrderSet orserSet) throws Exception;
}
