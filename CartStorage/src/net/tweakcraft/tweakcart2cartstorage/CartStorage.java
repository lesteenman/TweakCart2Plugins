/*
 * Copyright (c) 2012.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package net.tweakcraft.tweakcart2cartstorage;

import net.tweakcraft.tweakcart.TweakCart;
import net.tweakcraft.tweakcart.api.TweakCartEvent;
import net.tweakcraft.tweakcart.api.event.TweakVehiclePassesSignEvent;
import net.tweakcraft.tweakcart.api.plugin.AbstractSignPlugin;
import net.tweakcraft.tweakcart.model.IntMap;
import net.tweakcraft.tweakcart.util.ChestUtil;
import net.tweakcraft.tweakcart.util.InventoryManager;
import net.tweakcraft.tweakcart2cartstorage.parser.ItemParser;
import org.bukkit.block.Chest;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Edoxile
 */
public class CartStorage extends AbstractSignPlugin {
    public CartStorage(TweakCart p) {
        super(p);
    }

    @Override
    public void onEnable() {
        //TODO: this has to be without brackets right? no need for stripping them (waste of cycles)
        //TODO: Also, something with String.toLowerCase();
        plugin.getPluginManager().registerEvent(this, TweakCartEvent.Sign.VehiclePassesSignEvent, "collect items", "deposit items");
    }

    @Override
    public String getPluginName() {
        return "CartStorage";
    }

    @Override
    public void onSignPass(TweakVehiclePassesSignEvent event) {
        if (event.getMinecart() instanceof StorageMinecart) {
            StorageMinecart storageMinecart = (StorageMinecart) event.getMinecart();
            Inventory cartInventory = storageMinecart.getInventory();
            IntMap[] maps = ItemParser.parseSign(event.getSign(), event.getDirection());
            List<Chest> chestList = ChestUtil.getChestsAroundBlock(event.getSign().getBlock(), 1);
            for (Chest c : chestList) {
                InventoryManager.moveContainerContents(cartInventory, c.getInventory(), maps);
                /**
                 * TODO: we still have to do something with the return data of this function. For example,
                 * if the cart is empty, we can omit the storing in chests for at least one round. Also,
                 * we should explain to the people which comes first, collect or deposit. It is of importance
                 * in some situations.
                 */
            }
        }
    }
}