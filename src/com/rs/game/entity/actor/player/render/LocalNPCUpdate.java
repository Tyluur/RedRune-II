package com.rs.game.entity.actor.player.render;

import com.rs.game.GameConstants;
import com.rs.game.entity.actor.mask.Hit;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.world.World;
import com.rs.networking.NetworkConstants;
import com.rs.networking.io.OutputStream;
import com.rs.utility.Misc;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class LocalNPCUpdate {
	
	private Player player;
	
	private LinkedList<NPC> localNPCs;
	
	public LocalNPCUpdate(Player player) {
		this.player = player;
		localNPCs = new LinkedList<NPC>();
	}
	
	public void reset() {
		localNPCs.clear();
	}
	
	public OutputStream createPacketAndProcess() {
		OutputStream stream = new OutputStream();
		OutputStream updateBlockData = new OutputStream();
		stream.writePacketVarShort(6);
		processLocalNPCsInform(stream, updateBlockData);
		stream.writeBytes(updateBlockData.getBuffer(), 0, updateBlockData.getOffset());
		stream.endPacketVarShort();
		return stream;
	}
	
	private void processLocalNPCsInform(OutputStream stream, OutputStream updateBlockData) {
		stream.initBitAccess();
		processInScreenNPCs(stream, updateBlockData);
		addInScreenNPCs(stream, updateBlockData);
		if (updateBlockData.getOffset() > 0) {
			stream.writeBits(15, 32767);
		}
		stream.finishBitAccess();
	}
	
	private void processInScreenNPCs(OutputStream stream, OutputStream updateBlockData) {
		stream.writeBits(8, localNPCs.size());
		// for (NPC n : localNPCs.toArray(new NPC[localNPCs.size()])) {
		for (Iterator<NPC> it = localNPCs.iterator(); it.hasNext(); ) {
			NPC n = it.next();
			if (n.hasFinished() || !player.withinDistance(n) || n.hasTeleported()) {
				stream.writeBits(1, 1);
				stream.writeBits(2, 3);
				it.remove();
				continue;
			}
			boolean needUpdate = (updateBlockData.getOffset() + stream.getOffset() < (NetworkConstants.PACKET_SIZE_LIMIT - 500)) && n.needMasksUpdate();
			boolean walkUpdate = n.getNextWalkDirection() != -1;
			stream.writeBits(1, (needUpdate || walkUpdate) ? 1 : 0);
			if (walkUpdate) {
				stream.writeBits(2, n.getNextRunDirection() == -1 ? 1 : 2);
				if (n.getNextRunDirection() != -1) {
					stream.writeBits(1, 1);
				}
				stream.writeBits(3, Misc.getNpcMoveDirection(n.getNextWalkDirection()));
				if (n.getNextRunDirection() != -1) {
					stream.writeBits(3, Misc.getNpcMoveDirection(n.getNextRunDirection()));
				}
				stream.writeBits(1, needUpdate ? 1 : 0);
			} else if (needUpdate) {
				stream.writeBits(2, 0);
			}
			if (needUpdate) {
				appendUpdateBlock(n, updateBlockData, false);
			}
		}
	}
	
	private void addInScreenNPCs(OutputStream stream, OutputStream updateBlockData) {
		for (int regionId : player.getMapRegionsIds()) {
			List<Integer> indexes = World.getRegion(regionId).getNPCsIndexes();
			if (indexes == null) {
				continue;
			}
			for (int npcIndex : indexes) {
				if (localNPCs.size() == GameConstants.LOCAL_NPCS_LIMIT || (updateBlockData.getOffset() + stream.getOffset() > (NetworkConstants.PACKET_SIZE_LIMIT - 500))) {
					break;
				}
				NPC n = World.getNPCs().get(npcIndex);
				if (n == null || n.hasFinished() || localNPCs.contains(n) || !player.withinDistance(n) || n.isDead()) {
					continue;
				}
				stream.writeBits(15, n.getIndex());
				stream.writeBits(3, (n.getDirection() >> 11) - 4);
				boolean needUpdate = n.needMasksUpdate() || n.getLastFaceEntity() != -1;
				stream.writeBits(1, needUpdate ? 1 : 0);
				int y = n.getY() - player.getY();
				if (y < 15) {
					y += 32;
				}
				stream.writeBits(5, y);
				stream.writeBits(2, n.getPlane());
				stream.writeBits(15, n.getId());
				int x = n.getX() - player.getX();
				if (x < 15) {
					x += 32;
				}
				stream.writeBits(5, x);
				stream.writeBits(1, n.hasTeleported() ? 1 : 0);
				localNPCs.add(n);
				if (needUpdate) {
					appendUpdateBlock(n, updateBlockData, true);
				}
			}
		}
	}
	
	private void appendUpdateBlock(NPC n, OutputStream data, boolean added) {
		int maskData = 0;
		if (n.getNextGraphics3() != null) {
			maskData |= 0x100000;
		}
		if (n.getNextFaceEntity() != -2 || (added && n.getLastFaceEntity() != -1)) {
			maskData |= 0x1;
		}
		if (n.getNextGraphics4() != null) {
			maskData |= 0x20000;
		}
		if (!n.getNextHits().isEmpty()) {
			maskData |= 0x40;
		}
		if (n.hasChangedName() || (added && n.getCustomName() != null)) {
			maskData |= 0x40000;
		}
		if (n.getNextTransformation() != null) {
			maskData |= 0x20;
		}
		if (n.getNextForceTalk() != null) {
			maskData |= 0x2;
		}
		if (n.getNextFaceWorldTile() != null && n.getNextRunDirection() == -1 && n.getNextWalkDirection() == -1) {
			maskData |= 0x8;
		}
		if (n.getNextForceMovement() != null) {
			maskData |= 0x400;
		}
		if (n.hasChangedCombatLevel() || (added && n.getCustomCombatLevel() >= 0)) {
			maskData |= 0x80000;
		}
		if (n.getNextAnimation() != null) {
			maskData |= 0x10;
		}
		if (n.getNextGraphics2() != null) {
			maskData |= 0x1000;
		}
		if (n.getNextGraphics1() != null) {
			maskData |= 0x4;
		}
		if (maskData >= 256) {
			maskData |= 0x80;
		}
		if (maskData >= 65536) {
			maskData |= 0x8000;
		}
		data.writeByte(maskData);
		if (maskData >= 256) {
			data.writeByte(maskData >> 8);
		}
		if (maskData >= 65536) {
			data.writeByte(maskData >> 16);
		}
		if (n.getNextGraphics3() != null) {
			applyGraphicsMask3(n, data);
		}
		if (n.getNextFaceEntity() != -2 || (added && n.getLastFaceEntity() != -1)) {
			applyFaceEntityMask(n, data);
		}
		if (n.getNextGraphics4() != null) {
			applyGraphicsMask4(n, data);
		}
		if (!n.getNextHits().isEmpty()) {
			applyHitMask(n, data);
		}
		if (n.hasChangedName() || (added && n.getCustomName() != null)) {
			applyNameChangeMask(n, data);
		}
		if (n.getNextTransformation() != null) {
			applyTransformationMask(n, data);
		}
		if (n.getNextForceTalk() != null) {
			applyForceTalkMask(n, data);
		}
		if (n.getNextFaceWorldTile() != null) {
			applyFaceWorldTileMask(n, data);
		}
		if (n.getNextForceMovement() != null) {
			applyForceMovementMask(n, data);
		}
		if (n.hasChangedCombatLevel() || (added && n.getCustomCombatLevel() >= 0)) {
			applyChangeLevelMask(n, data);
		}
		if (n.getNextAnimation() != null) {
			applyAnimationMask(n, data);
		}
		if (n.getNextGraphics2() != null) {
			applyGraphicsMask2(n, data);
		}
		if (n.getNextGraphics1() != null) {
			applyGraphicsMask1(n, data);
		}
	}
	
	private void applyChangeLevelMask(NPC n, OutputStream data) {
		data.writeShortLE(n.getCombatLevel());
	}
	
	private void applyNameChangeMask(NPC npc, OutputStream data) {
		data.writeString(npc.getName());
	}
	
	private void applyTransformationMask(NPC n, OutputStream data) {
		data.writeShortLE(n.getNextTransformation().getToNPCId());
	}
	
	private void applyForceTalkMask(NPC n, OutputStream data) {
		data.writeString(n.getNextForceTalk().getText());
	}
	
	private void applyForceMovementMask(NPC n, OutputStream data) {
		data.write128Byte(n.getNextForceMovement().getToFirstTile().getX() - n.getX());
		data.write128Byte(n.getNextForceMovement().getToFirstTile().getY() - n.getY());
		data.writeByteC(n.getNextForceMovement().getToSecondTile() == null ? 0 : n.getNextForceMovement().getToSecondTile().getX() - n.getX());
		data.writeByteC(n.getNextForceMovement().getToSecondTile() == null ? 0 : n.getNextForceMovement().getToSecondTile().getY() - n.getY());
		data.writeShort((n.getNextForceMovement().getFirstTileTicketDelay() * 600) / 20);
		data.writeShortLE128(n.getNextForceMovement().getToSecondTile() == null ? 0 : ((n.getNextForceMovement().getSecondTileTicketDelay() * 600) / 20));
		data.write128Byte(n.getNextForceMovement().getDirection());
	}
	
	private void applyFaceWorldTileMask(NPC n, OutputStream data) {
		data.writeShortLE(n.getNextFaceWorldTile().getX() * 2 + 1);
		data.writeShortLE(n.getNextFaceWorldTile().getY() * 2 + 1);
	}
	
	private void applyHitMask(NPC n, OutputStream data) {
		int count = n.getNextHits().size();
		data.writeByteC(count);
		if (count > 0) {
			int hp = n.getHitpoints();
			int maxHp = n.getMaxHitpoints();
			if (hp > maxHp) {
				hp = maxHp;
			}
			int hpBarPercentage = hp * 255 / maxHp;
			for (Hit hit : n.getNextHits()) {
				boolean interactingWith = hit.interactingWith(player, n);
				if (hit.missed() && !interactingWith) {
					data.writeSmart(32766);
				} else {
					if (hit.getSoaking() != null) {
						data.writeSmart(32767);
						data.writeSmart(hit.getMark(player, n));
						data.writeSmart(hit.getDamage());
						data.writeSmart(hit.getSoaking().getMark(player, n));
						data.writeSmart(hit.getSoaking().getDamage());
					} else {
						data.writeSmart(hit.getMark(player, n));
						data.writeSmart(hit.getDamage());
					}
				}
				data.writeSmart(hit.getDelay());
				data.writeByte(hpBarPercentage);
			}
		}
	}
	
	private void applyFaceEntityMask(NPC n, OutputStream data) {
		data.writeShortLE(n.getNextFaceEntity() == -2 ? n.getLastFaceEntity() : n.getNextFaceEntity());
	}
	
	private void applyAnimationMask(NPC n, OutputStream data) {
		for (int id : n.getNextAnimation().getIds()) {
			data.writeShort(id);
		}
		data.writeByte(n.getNextAnimation().getSpeed());
	}
	
	private void applyGraphicsMask4(NPC n, OutputStream data) {
		data.writeShort(n.getNextGraphics4().getId());
		data.writeInt(n.getNextGraphics4().getSettingsHash());
		data.writeByte(n.getNextGraphics4().getSettings2Hash());
	}
	
	private void applyGraphicsMask3(NPC n, OutputStream data) {
		data.writeShort(n.getNextGraphics3().getId());
		data.writeInt(n.getNextGraphics3().getSettingsHash());
		data.writeByte128(n.getNextGraphics3().getSettings2Hash());
	}
	
	private void applyGraphicsMask2(NPC n, OutputStream data) {
		data.writeShort128(n.getNextGraphics2().getId());
		data.writeInt(n.getNextGraphics2().getSettingsHash());
		data.writeByte(n.getNextGraphics2().getSettings2Hash());
	}
	
	private void applyGraphicsMask1(NPC n, OutputStream data) {
		data.writeShortLE(n.getNextGraphics1().getId());
		data.writeIntV2(n.getNextGraphics1().getSettingsHash());
		data.writeByteC(n.getNextGraphics1().getSettings2Hash());
	}
	
}
