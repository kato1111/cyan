package com.internousdev.cyan.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.cyan.dao.CartInfoDAO;
import com.internousdev.cyan.dao.PurchaseHistoryInfoDAO;
import com.internousdev.cyan.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class SettlementCompleteAction extends ActionSupport implements SessionAware{

	private String id;
	private Map<String, Object> session;

	public String execute() {

		String result = SUCCESS;

		if(!session.containsKey("mCategoryDTOList")) {
			return "timeout";
		}

		@SuppressWarnings("unchecked")
		ArrayList<CartInfoDTO> cartInfoDTOList = (ArrayList<CartInfoDTO>)session.get("cartInfoDTOList");

		//sessionからloginIdを取得
		String loginId = String.valueOf(session.get("loginId"));

		//カートから購入した数だけ商品を商品購入履歴に登録する
		PurchaseHistoryInfoDAO purchaseHistoryInfoDAO = new PurchaseHistoryInfoDAO();
		int count = 0;
		//順番に登録していく
		for(int i=0; i<cartInfoDTOList.size();i++) {
			count += purchaseHistoryInfoDAO.regist(
					loginId,
					cartInfoDTOList.get(i).getProductId(),
					cartInfoDTOList.get(i).getProductCount(),
					Integer.parseInt(id),
					cartInfoDTOList.get(i).getSubtotal()
					);
		}

		//カートから商品を削除する(deleteAll)
		if(count > 0) {
			CartInfoDAO cartInfoDAO = new CartInfoDAO();
			count = cartInfoDAO.deleteAll(loginId);



			// ここの処理は不要　(再度カートに商品を登録する必要はない)
//					if(count > 0) {
//				cartInfoDTOList = new ArrayList<CartInfoDTO>();
//				cartInfoDTOList = (ArrayList<CartInfoDTO>) cartInfoDAO.getCartInfoDTOList(loginId);
//				Iterator<CartInfoDTO> iterator = cartInfoDTOList.iterator();
//				if(!(iterator.hasNext())) {
//					cartInfoDTOList = null;
//				}
//				session.put("cartInfoDTOList", cartInfoDTOList);
//
//				int totalPrice = Integer.parseInt(String.valueOf(cartInfoDAO.getTotalPrice(loginId)));
//				session.put("totalPrice", totalPrice);
//				session.remove("purchaseHistoryInfoDTOList");


		}return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

}
