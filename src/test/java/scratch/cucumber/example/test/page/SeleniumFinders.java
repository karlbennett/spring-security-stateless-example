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
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeleniumFinders implements Finders {

    private final WebDriver driver;
    private final Bys bys;

    @Autowired
    public SeleniumFinders(WebDriver driver, Bys bys) {
        this.driver = driver;
        this.bys = bys;
    }

    @Override
    public String findIdByLabel(String text) {
        return driver.findElement(bys.byLabel(text)).getAttribute("for");
    }

    @Override
    public void enterTextByLabel(String labelName, String text) {
        driver.findElement(By.id(findIdByLabel(labelName))).sendKeys(text);
    }
}
