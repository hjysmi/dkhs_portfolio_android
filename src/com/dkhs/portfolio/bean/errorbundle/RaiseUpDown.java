/**
 * @Title RaiseUpDown.java
 * @Package com.dkhs.portfolio.bean.errorbundle
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-12-2 上午11:21:38
 * @version V1.0
 */
package com.dkhs.portfolio.bean.errorbundle;

import java.util.List;

/**
 * @ClassName RaiseUpDown
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-12-2 上午11:21:38
 * @version 1.0
 */
public class RaiseUpDown {
    private List<String> raise_up;
    private List<String> raise_down;

    public List<String> getRaise_up() {
        return raise_up;
    }

    public void setRaise_up(List<String> raise_up) {
        this.raise_up = raise_up;
    }

    public List<String> getRaise_down() {
        return raise_down;
    }

    public void setRaise_down(List<String> raise_down) {
        this.raise_down = raise_down;
    }

}
