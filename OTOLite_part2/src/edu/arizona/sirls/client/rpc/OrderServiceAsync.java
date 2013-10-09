package edu.arizona.sirls.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.arizona.sirls.shared.beans.orders.OrderSet;

public interface OrderServiceAsync {

	void getOrderSets(String uploadID,
			AsyncCallback<ArrayList<OrderSet>> callback) throws Exception;

	void saveOrderSet(OrderSet orserSet, AsyncCallback<Void> callback)
			throws Exception;

}
