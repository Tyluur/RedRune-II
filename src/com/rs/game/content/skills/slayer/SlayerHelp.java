package com.rs.game.content.skills.slayer;

public class SlayerHelp {
	
	public static int getXp(int id) {
		switch (id) {
			case -1:
				return -1;
			case 2783:
				return 570;
			case 1604:
			case 1605:
			case 1606:
			case 1607:
				return 197;
			case 1610:
				return 235;
			case 1612:
				return 95;
			case 1613:
				return 310;
			case 1615:
				return 427;
			case 1618:
				return 194;
			case 1643:
			case 1644:
			case 1645:
			case 1646:
			case 1647:
				return 176;
			case 1648:
			case 1649:
			case 1650:
			case 1651:
			case 1652:
			case 1653:
			case 1654:
			case 1655:
			case 1656:
			case 1657:
				return 69;
			default:
				return 0;
		}
	}
	
	public static int getPoints(int id) {
		switch (id) {
			case -1:
				return -1;
			case 2783:
				return 50;
			case 1604:
			case 1605:
			case 1606:
			case 1607:
				return 17;
			case 1610:
				return 23;
			case 1612:
				return 9;
			case 1613:
				return 31;
			case 1615:
				return 42;
			case 1618:
				return 19;
			case 1643:
			case 1644:
			case 1645:
			case 1646:
			case 1647:
				return 17;
			case 1648:
			case 1649:
			case 1650:
			case 1651:
			case 1652:
			case 1653:
			case 1654:
			case 1655:
			case 1656:
			case 1657:
				return 6;
			default:
				return 0;
		}
	}
	
	public static int getReq(int id) {
		switch (id) {
			case -1:
				return -1;
			case 2783:
				return 80;
			case 1604:
			case 1605:
			case 1606:
			case 1607:
				return 35;
			case 1610:
				return 53;
			case 1612:
				return 10;
			case 1613:
				return 55;
			case 1615:
				return 75;
			case 1618:
				return 39;
			case 1643:
			case 1644:
			case 1645:
			case 1646:
			case 1647:
				return 30;
			case 1648:
			case 1649:
			case 1650:
			case 1651:
			case 1652:
			case 1653:
			case 1654:
			case 1655:
			case 1656:
			case 1657:
				return 0;
			default:
				return 0;
		}
	}
	
	public static boolean isSlayer(int id) {
		switch (id) {
			case 2783:
			case 1604:
			case 1605:
			case 1606:
			case 1607:
			case 1610:
			case 3406:
			case 1612:
			case 1613:
			case 1615:
			case 1618:
			case 1643:
			case 1644:
			case 1645:
			case 1646:
			case 1647:
			case 1648:
			case 1649:
			case 1650:
			case 1651:
			case 1652:
			case 1653:
			case 1654:
			case 1655:
			case 1656:
			case 1657:
				return true;
			default:
				return false;
		}
	}
}
