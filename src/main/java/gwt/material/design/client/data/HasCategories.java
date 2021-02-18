/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2017 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package gwt.material.design.client.data;

import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.ComponentFactory;
import gwt.material.design.client.data.factory.CategoryComponentFactory;
import gwt.material.design.client.data.factory.Category;

import java.util.List;

public interface HasCategories<T> {

    /**
     * Set your own custom {@link CategoryComponentFactory} to generate your categories.
     */
    void setCategoryFactory(ComponentFactory<? extends CategoryComponent<T>, Category> categoryFactory);

    /**
     * Get a stored category component.
     * @param categoryName name of the category component.
     */
    CategoryComponent<T> getCategory(String categoryName);

    /**
     * Get all registered category components.
     */
    Categories<T> getCategories();

    /**
     * Get all the open {@link CategoryComponent}'s or null if categories are disabled.
     */
    Categories<T> getOpenCategories();

    /**
     * Check if a category has data to provide.
     */
    boolean isCategoryEmpty(CategoryComponent<T> category);

    /**
     * Explicitly add a category, which will be drawn to the table.
     * If the category already exists then it will be ignored.
     * @param category The category name.
     */
    void addCategory(String category);

    /**
     * Explicitly add a category, which will be drawn to the table.
     * If the category already exists then it will be ignored.
     * @param category The category pair info.
     */
    void addCategory(Category category);

    /**
     * Explicitly add a {@link CategoryComponent}, which will be drawn to the table.
     * If the category already exists then it will be ignored.
     * @param category The category data defined.
     */
    void addCategory(CategoryComponent<T> category);

    /**
     * Has this data view got an existing {@link CategoryComponent} with given name.
     */
    boolean hasCategory(String categoryName);

    /**
     * Disable a data view category.
     */
    void disableCategory(String categoryName);

    /**
     * Enable a data view category.
     */
    void enableCategory(String categoryName);

    /**
     * Open an existing Category.
     */
    void openCategory(String categoryName);

    /**
     * Open an existing Category.
     */
    void openCategory(CategoryComponent<T> category);

    /**
     * Close an existing Category.
     */
    void closeCategory(String categoryName);

    /**
     * Close an existing Category.
     */
    void closeCategory(CategoryComponent<T> category);

    /**
     * Open all the categories.
     */
    void openAllCategories();

    /**
     * Open all the categories.
     */
    void closeAllCategories();

    /**
     * Clear all rows and categories.
     *
     * @param clearData should we also clear the stored data.
     */
    void clearRowsAndCategories(boolean clearData);

    /**
     * Clear all categories.
     */
    void clearCategories();
}
