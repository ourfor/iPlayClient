import { ThemeBasicStyle } from '@global';
import { NativeStackNavigationOptions } from '@react-navigation/native-stack';
import { createSelector, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { RootState } from '@store';
import _, { get } from 'lodash';
import { useMemo } from 'react';
import { EdgeInsets } from 'react-native-safe-area-context';

interface ThemeState {
    routeName: string;
    hideMenuBar: boolean;
    // menu bar padding bottom offset
    menuBarPaddingOffset: number;
    menuBarHeight?: number;
    statusBarHeight: number,
    pagePaddingTop: number,
    safeInsets: EdgeInsets;
    showVideoLink?: boolean;
    isDarkMode: boolean;
    fontColor?: string;
    fontSize?: number;
    fontFamily?: string;
    backgroundColor?: string;
    barStyle?: 'default' | 'light-content' | 'dark-content';
    headerTitleAlign?: 'left' | 'center';
}

type ThemeUpdateFunction = (state: ThemeState) => ThemeState;

const initialState: ThemeState = {
    routeName: 'home',
    safeInsets: {
        top: 0,
        right: 0,
        bottom: 0,
        left: 0
    },
    hideMenuBar: false,
    menuBarPaddingOffset: 0,
    statusBarHeight: 0,
    showVideoLink: false,
    isDarkMode: false,
    headerTitleAlign: 'center',
    pagePaddingTop: 56,
};

export const slice = createSlice({
    name: 'theme',
    initialState,
    reducers: {
        switchRoute: (state, action: PayloadAction<string>) => {
            const routeName = action.payload;
            const whitelist = ['home', 'settings', 'search', 'star', 'message', 'test'];
            if (whitelist.includes(routeName)) {
                state.hideMenuBar = false;
            } else {
                state.hideMenuBar = true;
            }
        },
        updateMenuBarPaddingOffset: (state, action: PayloadAction<number>) => {
            state.menuBarPaddingOffset = action.payload;
        },
        updateMenuBarHeight: (state, action: PayloadAction<number>) => {
            state.menuBarHeight = action.payload;
        },
        updateShowVideoLink: (state, action: PayloadAction<boolean>) => {
            state.showVideoLink = action.payload;
        },
        updateTheme:(state, action: PayloadAction<Partial<ThemeState>|ThemeUpdateFunction>) => {
            if (typeof action.payload === 'function') {
                action.payload(state);
            } else {
                _.merge(state, action.payload)
            }
        }
    },
});

const getHeaderTintColor = (state: RootState) => state.theme.fontColor;
const getBackgroundColor = (state: RootState) => state.theme.backgroundColor;
const getHeaderTitleAlign = (state: RootState) => state.theme.headerTitleAlign;

export const selectScreenOptions = createSelector([
    getHeaderTintColor,
    getBackgroundColor,
    getHeaderTitleAlign
], (headerTintColor, backgroundColor, headerTitleAlign) => {
    const options = {
        headerTitleAlign: headerTitleAlign,
        headerStyle: {
            backgroundColor: "transparent"
        }, 
        headerTransparent: true,
        headerTintColor,
        contentStyle: {
            backgroundColor: "transparent",
        },
    }
    return options as {};
})

export const selectThemeBasicStyle = createSelector([
    getHeaderTintColor,
    getBackgroundColor
], (color, backgroundColor) => {
    return {color, backgroundColor} as ThemeBasicStyle;
})

export const { 
    switchRoute, 
    updateMenuBarPaddingOffset, 
    updateMenuBarHeight,
    updateShowVideoLink,
    updateTheme
} = slice.actions;

export default slice.reducer;