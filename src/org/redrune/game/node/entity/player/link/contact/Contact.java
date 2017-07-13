package org.redrune.game.node.entity.player.link.contact;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public final class Contact {
	
	/**
	 * The name of the contact
	 */
	@Getter
	private final String username;
	
	/**
	 * If the contact is online
	 */
	@Getter
	@Setter
	private transient boolean online;
	
	/**
	 * The world the contact is on
	 */
	@Getter
	@Setter
	private transient byte worldId;
	
	public Contact(String username) {
		this.username = username;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Contact) {
			final Contact contact = (Contact) obj;
			if (Objects.equals(contact.getUsername(), username) && contact.getWorldId() == worldId) {
				return true;
			}
		}
		return false;
	}
}
