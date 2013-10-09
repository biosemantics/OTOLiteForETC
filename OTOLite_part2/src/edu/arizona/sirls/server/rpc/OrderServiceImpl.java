package edu.arizona.sirls.server.rpc;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.sirls.client.rpc.OrderService;
import edu.arizona.sirls.server.db.OrderDAO;
import edu.arizona.sirls.shared.beans.orders.OrderSet;

public class OrderServiceImpl extends RemoteServiceServlet implements
		OrderService {

	private static final long serialVersionUID = 6156458035937676513L;

	/**
	 * get the ordersets for order page when loading
	 * 
	 * @throws Exception
	 */
	@Override
	public ArrayList<OrderSet> getOrderSets(String uploadID) throws Exception {
		ArrayList<OrderSet> orderSets = new ArrayList<OrderSet>();

		OrderDAO orderDAO = OrderDAO.getInstance();
		orderSets = orderDAO.getOrderSets(Integer.parseInt(uploadID));
		return orderSets;
	}

	/**
	 * save orderSet to database
	 * 
	 * @throws Exception
	 */
	@Override
	public void saveOrderSet(OrderSet orderSet) throws Exception {
		OrderDAO orderDAO = OrderDAO.getInstance();
		orderDAO.saveOrderSet(orderSet);
	}

}
