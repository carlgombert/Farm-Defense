.PHONY: build clean run run-build help

PROJECT_DIR := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))
FARM_DIR := $(PROJECT_DIR)farm_defense
SRC_DIR := $(FARM_DIR)/src
BIN_DIR := $(FARM_DIR)/bin

JAVAC := javac
JAVA := java
MAIN_CLASS := controller.Game

# Colors for output
GREEN := \033[0;32m
YELLOW := \033[1;33m
RED := \033[0;31m
NC := \033[0m # No Color

help:
	@echo "Farm Defense Build System"
	@echo ""
	@echo "Available targets:"
	@echo "  make build       - Compile Java source files"
	@echo "  make clean       - Remove compiled classes"
	@echo "  make run         - Run the game (requires build)"
	@echo "  make run-build   - Build and run the game"
	@echo "  make help        - Display this help message"
	@echo ""
	@echo "Examples:"
	@echo "  make build       - Build the project"
	@echo "  make run-build   - Clean build and run"

build: $(BIN_DIR)
	@echo "$(YELLOW)Compiling Java source files...$(NC)"
	@$(JAVAC) -d $(BIN_DIR) -sourcepath $(SRC_DIR) $$(find $(SRC_DIR) -name "*.java")
	@echo "$(GREEN)Build successful!$(NC)"
	@echo "Output: $(BIN_DIR)"

$(BIN_DIR):
	@mkdir -p $(BIN_DIR)

clean:
	@echo "$(YELLOW)Cleaning build directory...$(NC)"
	@rm -rf $(BIN_DIR)
	@echo "$(GREEN)Clean complete.$(NC)"

run: $(BIN_DIR)
	@if [ ! -f "$(BIN_DIR)/controller/Game.class" ]; then \
		echo "$(RED)Error: Game not compiled. Run 'make build' first.$(NC)"; \
		exit 1; \
	fi
	@echo "$(YELLOW)Starting Farm Defense...$(NC)"
	@echo ""
	@cd $(FARM_DIR) && $(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS)

run-build: clean build run

.DEFAULT_GOAL := help
