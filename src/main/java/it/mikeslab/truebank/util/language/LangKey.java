/*
 * GNU GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * GNU GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * GNU GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.mikeslab.truebank.util.language;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LangKey {
    PREFIX("&c&lTrueBank &8» &r"),
    WIRETRANSFER_DESCRIPTION("WireTransfer"),
    CARDTYPE_SELECTOR_TITLE("Select a Card Type"),
    CARDTYPE_PRICE("&#FF9600Price&#FF5400: &7%price%"),
    CARDTYPE_CLICK_SELECT("&#FFBA00(Click to select this card type)"),
    PREVIOUS_PAGE("&cPrevious Page"),
    NEXT_PAGE("&aNext Page"),
    BANKNOTE_SELECTOR_TITLE("Withdraw a Banknote"),
    BANKNOTE_VALUE("&#FF9600Value&#FF5400: &7%value%"),
    BANKNOTE_CLICK_SELECT("&#FFBA00(Click to withdraw)"),
    BALANCE("&aBalance"),
    BALANCE_LORE("&7%balance%"),
    TRANSACTION_MENU_TITLE("%target%'s transactions"),
    TRANSACTION_DESCRIPTION("&fDescription&a: &7%description%"),
    TRANSACTION_AMOUNT("&fAmount&a: &7%amount%"),
    TRANSACTION_SENDER("&fSender&a: &7%sender%"),
    TRANSACTION_RECEIVER("&fReceiver&a: &7%receiver%"),
    TRANSACTION_ID("&fID&a: &7%id%"),
    TRANSACTION_CLICK_DELETE("&c(Right-Click to Delete)"),
    TRANSACTION_HEADER("&8Transaction"),
    WRONG_PIN("Wrong PIN..."),
    PIN_GUI_TITLE("Insert PIN"),
    INVENTORY_FULL("&cYour Inventory is full! Cannot perform this action."),
    HOLDER_WITHDRAW_LIMIT_REACHED("The account owner has reached the daily limit on withdrawals!");

    private final String defaultValue;

}
