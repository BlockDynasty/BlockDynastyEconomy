/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package abstractions.platform.recipes;

public class RecipeInventory {
    private final String title;
    private final int rows;

    private RecipeInventory(String title, int rows) {
        this.title = title;
        this.rows = rows;
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String title = "Economy GUI";
        private int rows = 6;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setRows(int rows) {
            this.rows = rows;
            return this;
        }

        public RecipeInventory build() {
            return new RecipeInventory(title, rows);
        }
    }
}
