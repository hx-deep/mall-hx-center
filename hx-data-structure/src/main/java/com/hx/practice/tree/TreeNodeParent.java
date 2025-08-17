package com.hx.practice.tree;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeNodeParent {

         int id;
         int parentId;
         String name;
         List<TreeNodeParent> children = new ArrayList<>();

        public TreeNodeParent(int id, int parentId, String name) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
        }



}
