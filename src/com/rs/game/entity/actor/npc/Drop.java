package com.rs.game.entity.actor.npc;

public class Drop {

	private int itemId, minAmount, maxAmount;
	private double rate;
	@SuppressWarnings("unused")
	private boolean rare;

	public static Drop create(int itemId, double rate, int minAmount,
			int maxAmount, boolean rare) {
		return new Drop((short) itemId, rate, minAmount, maxAmount, rare);
	}

	public Drop(int itemId, double rate, int minAmount, int maxAmount,
			boolean rare) {
		this.itemId = itemId;
		this.rate = rate;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.rare = rare;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public int getExtraAmount() {
		return maxAmount - minAmount;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public int getItemId() {
		return itemId;
	}

	public double getRate() {
		return rate;
	}

	public void setItemId(short itemId) {
		this.itemId = itemId;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setMinAmount(int amount) {
		this.minAmount = amount;
	}

	public void setMaxAmount(int amount) {
		this.maxAmount = amount;
	}

	public boolean isFromRareTable() {
		return rare;
	}

}