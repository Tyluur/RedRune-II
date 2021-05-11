package utility.constants;

import cache.codec.loaders.ItemDefinitions;
import game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 6/15/2017
 */
public interface ItemConstants {
	
	/**
	 * The id of coins
	 */
	int COINS = 995;
	
	/**
	 * The id of the gold ticket
	 */
	int GOLD_TICKET = 13663;
	
	/**
	 * This method checks for untradeables that should drop on death.
	 *
	 * @param item
	 * 		The item
	 */
	static boolean untradeableDropsOnDeath(Item item) {
		switch (item.getId()) {
			case 20137:
			case 20141:
			case 20145:
			case 20149:
			case 20153:
			case 20157:
			case 20165:
			case 20169:
			case 18363:
			case 13860:
			case 18335:
			case 13863:
			case 13866:
			case 13869:
			case 13872:
			case 13875:
			case 13878:
			case 13886:
			case 13889:
			case 13892:
			case 13895:
			case 13898:
			case 13901:
			case 13904:
			case 13907:
			case 13910:
			case 13913:
			case 13916:
			case 13919:
			case 13922:
			case 13925:
			case 13928:
			case 13931:
			case 13934:
			case 13937:
			case 13940:
			case 13943:
			case 13946:
			case 13949:
			case 13952:
			case 4856:
			case 4857:
			case 4858:
			case 4859:
			case 4862:
			case 4863:
			case 4864:
			case 4865:
			case 4868:
			case 4869:
			case 4870:
			case 4871:
			case 4874:
			case 4875:
			case 4876:
			case 4877:
			case 4880:
			case 4881:
			case 4882:
			case 4883:
			case 4886:
			case 4887:
			case 4888:
			case 4889:
			case 4892:
			case 4893:
			case 4894:
			case 4895:
			case 4898:
			case 4899:
			case 4900:
			case 4901:
			case 4904:
			case 4905:
			case 4906:
			case 4907:
			case 4910:
			case 4911:
			case 4912:
			case 4913:
			case 4916:
			case 4917:
			case 4918:
			case 4919:
			case 4922:
			case 4923:
			case 4924:
			case 4925:
			case 4928:
			case 4929:
			case 4930:
			case 4931:
			case 4934:
			case 4935:
			case 4936:
			case 4937:
			case 4940:
			case 4941:
			case 4942:
			case 4943:
			case 4946:
			case 4947:
			case 4948:
			case 4949:
			case 4952:
			case 4953:
			case 4954:
			case 4955:
			case 4958:
			case 4959:
			case 4960:
			case 4961:
			case 4964:
			case 4965:
			case 4966:
			case 4967:
			case 4970:
			case 4971:
			case 4972:
			case 4973:
			case 4976:
			case 4977:
			case 4978:
			case 4979:
			case 4982:
			case 4983:
			case 4984:
			case 4985:
			case 4988:
			case 4989:
			case 4990:
			case 4991:
			case 4994:
			case 4995:
			case 4996:
			case 4997:
				return true;
			default:
				return false;
		}
	}
	
	static int getDegradeItemWhenWear(int id) {
		// pvp armors
		/*
		 * if (id == 13958 || id == 13961 || id == 13964 || id == 13967 || id ==
		 * 13970 || id == 13973 || id == 13858 || id == 13861 || id == 13864 ||
		 * id == 13867 || id == 13870 || id == 13873 || id == 13876 || id ==
		 * 13884 || id == 13887 || id == 13890 || id == 13893 || id == 13896 ||
		 * id == 13899 || id == 13902 || id == 13905 || id == 13908 || id ==
		 * 13911 || id == 13914 || id == 13917 || id == 13920 || id == 13923 ||
		 * id == 13926 || id == 13929 || id == 13932 || id == 13935 || id ==
		 * 13938 || id == 13941 || id == 13944 || id == 13947 || id == 13950 ||
		 * id == 13958) return id + 2; // if you wear it it becomes corrupted
		 * LOL
		 */
		return -1;
	}
	
	// return what id it degrades to, -1 for disapear which is default so we
	// dont add -1
	static int getItemDegrade(int id) {
		if (id == 11285) // DFS
		{
			return 11283;
		}
		// nex armors
		/*
		 * if (id == 20137 || id == 20141 || id == 20145 || id == 20149 || id ==
		 * 20153 || id == 20157 || id == 20161 || id == 20165 || id == 20169 ||
		 * id == 20173) return id + 1;
		 */
		return -1;
	}
	
	static int getDegradeItemWhenCombating(int id) {
		// nex armors
		/*
		 * if (id == 20135 || id == 20139 || id == 20143 || id == 20147 || id ==
		 * 20151 || id == 20155 || id == 20159 || id == 20163 || id == 20167 ||
		 * id == 20171) return id + 2;
		 */
		return -1;
	}
	
	static boolean itemDegradesWhileHit(int id) {
		return id == 2550;
	}
	
	static boolean itemDegradesWhileWearing(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		/*
		 * if (name.contains("c. dragon") || name.contains("corrupt dragon") ||
		 * name.contains("vesta's") || name.contains("statius'") ||
		 * name.contains("morrigan's") || name.contains("zuriel's")) return
		 * true;
		 */
		return false;
	}
	
	static boolean itemDegradesWhileCombating(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		// nex armors
		/*
		 * if (name.contains("torva") || name.contains("pernix") ||
		 * name.contains("virtux") || name.contains("zaryte")) return true;
		 */
		return false;
	}
	
	static boolean isTradeable(Item item) {
		if (item.getDefinitions().isDestroyItem() || item.getDefinitions().isLended() || getItemDefaultCharges(item.getId()) != -1) {
			return false;
		}
		switch (item.getId()) {
			default:
				return true;
		}
	}
	
	// return amt of charges
	static int getItemDefaultCharges(int id) {
		/*
		 * // pvp armors if (id == 13910 || id == 13913 || id == 13916 || id ==
		 * 13919 || id == 13922 || id == 13925 || id == 13928 || id == 13931 ||
		 * id == 13934 || id == 13937 || id == 13940 || id == 13943 || id ==
		 * 13946 || id == 13949 || id == 13952) return 1500; if (id == 13960 ||
		 * id == 13963 || id == 13966 || id == 13969 || id == 13972 || id ==
		 * 13975) return 3000; if (id == 13860 || id == 13863 || id == 13866 ||
		 * id == 13869 || id == 13872 || id == 13875 || id == 13878 || id ==
		 * 13886 || id == 13889 || id == 13892 || id == 13895 || id == 13898 ||
		 * id == 13901 || id == 13904 || id == 13907 || id == 13960) return
		 * 6000; // 1hour // nex armors if (id == 20137 || id == 20141 || id ==
		 * 20145 || id == 20149 || id == 20153 || id == 20157 || id == 20161 ||
		 * id == 20165 || id == 20169 || id == 20173) return 60000;
		 */
		return -1;
	}
}
