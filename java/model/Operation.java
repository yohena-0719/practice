package model;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

/**
 * 店内オペレーションクラス
 * @author M.Takahashi
 */
public class Operation {
	/**
	 * ログイン時の処理
	 * @param userId リクエストパラメータ
	 * @param password リクエストパラメータ
	 * @param session セッションオブジェクト
	 * @return true .. 正常、false .. ID／パスワード誤り
	 */
	public boolean loginProc(String userId, String password, HttpSession session) {

		// ログイン認証
		boolean result = authenticate(userId, password);

		if (result) {
			// 店舗データの作成⇒セッションに格納
			Store store = makeStore();
			session.setAttribute("store", store);
			
			// カート情報の作成（userId設定・商品リストは空）⇒セッションに格納
			Cart cart = new Cart(userId, new ArrayList<Product>());
			session.setAttribute("cart", cart);
		}

		return result;
	}

	/**
	 * 認証する
	 * @param userId ユーザID
	 * @param password パスワード
	 * @return 結果 (true / false)
	 */
	private boolean authenticate(String userId, String password) {

		// ★ここでは password = "pass" であれば true とする
		boolean result = password.equals("pass");

		return result;
	}
	
	/**
	 * 店舗情報（店舗名＋選択データ（リスト））を作成する
	 * @return 店舗情報
	 */
	private Store makeStore() {

		// 店舗情報作成
		Store store = new Store("速水PC販売", new ArrayList<Product>());
		
		// 商品追加
		store.add(new Product("A110", "無線マウス", 2000));
		store.add(new Product("A120", "薄型キーボード", 3600));
		store.add(new Product("A130", "Webカメラ", 3900));
		store.add(new Product("A140", "トラックボールマウス", 2900));
		store.add(new Product("A150", "USB接続HDD（外付け）", 9800));
		store.add(new Product("A160", "2m電源タップ５口", 1900));
		store.add(new Product("A170", "USB接続マイク", 3500));
		store.add(new Product("A180", "小型ディスプレイ", 11000));
		store.add(new Product("A190", "LED照明", 4200));
		store.add(new Product("A200", "骨伝導イヤホン", 7800));
		
		return store;
	}
	
	/**
	 * ログアウト時の処理
	 * @param session
	 */
	public void logoutProc(HttpSession session) {

		session.invalidate();
		
	}

	/**
	 * 商品追加処理
	 * @param idx 商品一覧の選択した商品のidx (セッション：store内)
	 * @param session セッションオブジェクト
	 */
	public void addProd(int idx, HttpSession session) {
		
		// 店舗情報・カート情報の取得（セッションより）
		Store store = (Store) session.getAttribute("store");
		Cart cart = (Cart) session.getAttribute("cart");

		if ((store != null) && (cart != null)) {
			// カートに指定の商品を追加
			cart.add(store.getListProd().get(idx));

			// セッションに再度格納
			session.setAttribute("cart", cart);
		}

	}

	/**
	 * カートから商品削除処理
	 * @param idx カートの中の選択した商品のidx
	 * @param session セッションオブジェクト
	 */
	public void removeProd(int idx, HttpSession session) {
	
		// カート内商品情報の取得（セッションより）
		Cart cart = (Cart) session.getAttribute("cart");
	
		if (cart != null) {
			// カートから指定の商品を削除
			cart.remove(idx);

			// セッションに書き戻す
			session.setAttribute("cart", cart);
		}
			
	}
	
	/**
	 * 精算処理
	 * @param session セッションオブジェクト
	 */
	public void pay(HttpSession session) {

		// カート内商品情報の取得
		Cart cart = (Cart) session.getAttribute("cart");

		if (cart != null) {
			// セッションに格納（精算済みデータ）
			session.setAttribute("pay", cart);

			// カート情報の新規作成⇒セッションに格納
			Cart newCart = new Cart(cart.getUserId(), new ArrayList<Product>());
			session.setAttribute("cart", newCart);
		}

	}

}
