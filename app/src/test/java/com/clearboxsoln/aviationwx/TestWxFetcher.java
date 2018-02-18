package com.clearboxsoln.aviationwx;

import org.junit.Test;
import com.clearboxsoln.aviationwx.WxFetcher;

import static org.junit.Assert.*;

/**
 * Created by mikes on 2/18/2018.
 */

public class TestWxFetcher {
    @Test
    public void print_metars() throws Exception {
        WxFetcher wx = new WxFetcher();
        wx.printMetars();
        assertEquals(4, 2 + 2);
    }
}
