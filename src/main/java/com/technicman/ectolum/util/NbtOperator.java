package com.technicman.ectolum.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.List;

public enum NbtOperator {
    REPLACE("replace") {
        public void merge(ItemStack itemStack, NbtPathArgumentType.NbtPath targetPath, NbtElement nbt) throws CommandSyntaxException {
            if (targetPath == null) {
                itemStack.setNbt((NbtCompound) nbt);
                return;
            }
            NbtCompound itemNbt = itemStack.getOrCreateNbt();
            targetPath.put(itemNbt, nbt);
        }
    },
    APPEND("append") {
        public void merge(ItemStack itemStack, NbtPathArgumentType.NbtPath targetPath, NbtElement nbt) throws CommandSyntaxException {
            NbtCompound itemNbt = itemStack.getOrCreateNbt();
            List<NbtElement> list = targetPath.getOrInit(itemNbt, NbtList::new);
            list.forEach((foundNbt) -> {
                if (foundNbt instanceof NbtList) {
                    ((NbtList)foundNbt).add(nbt.copy());
                }

            });
        }
    },
    MERGE("merge") {
        public void merge(ItemStack itemStack, NbtPathArgumentType.NbtPath targetPath, NbtElement nbt) throws CommandSyntaxException {
            NbtCompound itemNbt = itemStack.getOrCreateNbt();
            if (targetPath == null) {
                itemNbt.copyFrom((NbtCompound) nbt);
                return;
            }
            List<NbtElement> list = targetPath.getOrInit(itemNbt, NbtCompound::new);
            list.forEach((foundNbt) -> {
                if (foundNbt instanceof NbtCompound) {
                    if (nbt instanceof NbtCompound) {
                        ((NbtCompound)foundNbt).copyFrom((NbtCompound)nbt);
                    }
                }
            });
        }
    };

    final String name;

    public abstract void merge(ItemStack itemStack, NbtPathArgumentType.NbtPath targetPath, NbtElement nbt) throws CommandSyntaxException;

    NbtOperator(String name) {
        this.name = name;
    }

    public static NbtOperator get(String name) {
        NbtOperator[] operators = values();

        for (NbtOperator operator : operators) {
            if (operator.name.equals(name)) {
                return operator;
            }
        }

        throw new IllegalArgumentException("Invalid merge strategy" + name);
    }
}