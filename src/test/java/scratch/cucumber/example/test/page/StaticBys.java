/*
 * Copyright (C) 2015  Karl Bennett
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package scratch.cucumber.example.test.page;

import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class StaticBys implements Bys {

    @Override
    public By byLabel(String text) {
        return By.xpath(format("//label[text()[contains(.,'%s')]]", text));
    }

    @Override
    public By byValue(String value) {
        return By.xpath(format("//input[@value='%s']", value));
    }
}
