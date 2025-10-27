/*
 * Copyright (c) 2013 L2jMobius
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package handlers.admincommandhandlers;

import org.l2jmobius.gameserver.handler.IAdminCommandHandler;
import org.l2jmobius.gameserver.model.WorldObject;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.Disconnection;
import org.l2jmobius.gameserver.network.serverpackets.LeaveWorld;

/**
 * This class handles following admin commands: - character_disconnect = disconnects target player
 * @version $Revision: 1.2.4.4 $ $Date: 2005/04/11 10:06:00 $
 */
public class AdminDisconnect implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_character_disconnect"
	};
	
	@Override
	public boolean onCommand(String command, Player activeChar)
	{
		if (command.equals("admin_character_disconnect"))
		{
			disconnectCharacter(activeChar);
		}
		
		return true;
	}
	
	@Override
	public String[] getCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void disconnectCharacter(Player activeChar)
	{
		final WorldObject target = activeChar.getTarget();
		Player player = null;
		if ((target != null) && target.isPlayer())
		{
			player = target.asPlayer();
		}
		else
		{
			return;
		}
		
		if (player == activeChar)
		{
			activeChar.sendSysMessage("You cannot logout your own character.");
		}
		else
		{
			activeChar.sendSysMessage("Character " + player.getName() + " disconnected from server.");
			Disconnection.of(player).storeAndDeleteWith(LeaveWorld.STATIC_PACKET);
		}
	}
}
