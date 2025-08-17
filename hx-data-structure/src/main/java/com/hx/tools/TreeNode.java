package com.hx.tools;


import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;

/**
 * 树结构对象
 *
 * @author zhangzhe
 * @date 2023/12/15
 */
@Setter
@Getter
public abstract class TreeNode<T extends TreeNode<T>> implements Comparable<T> {

    private Collection<T> children;

    protected abstract String getId();

    protected abstract String getParentId();

    @Override
    public int compareTo(@Nonnull TreeNode o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TreeNode)) {
            return false;
        }
        return Objects.equals(this.getId(), ((TreeNode<?>) obj).getId());
    }

    private T parent;

    private boolean enabled = false;

    @Override
    public String toString() {
        return "\n" + toString(1) + "\n";
    }

    public String toString(int i) {
        StringBuilder builder = new StringBuilder();
        builder.append(getId());
        if (children != null) {
            children.forEach(t -> {
                builder.append("\n");
                for (int j = 0; j < i; j++) {
                    builder.append("--");
                }
                builder.append(t.toString(i + 1));
            });
        }
        return builder.toString();
    }
}
