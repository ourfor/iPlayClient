import { Device, OSType, isOS } from "@helper/device";
import { useAppSelector } from "@hook/store";
import { ComponentProps, useEffect, useMemo, useState } from "react";
import { Platform, StatusBar as StatusBarOrigin, NativeModules, Text, View, StyleSheet, ViewStyle, Pressable } from "react-native";
import GoBackIcon from "@asset/reset.svg"
import { useNavigation } from "@react-navigation/native";
import { selectThemedPageStyle } from "@store/themeSlice";
import NativeTitleBar from "@api/native/windows/NativeTitleBar";
import { logger } from "@helper/log";

export const StatusBarHeight = 
    Platform.OS === "android" ? (StatusBarOrigin.currentHeight ?? 0) : (isOS(OSType.Windows) ? 0 :NativeModules.UIModule.statusBarHeight());

export function MobileStatusBar(props: ComponentProps<typeof StatusBarOrigin>) {
    const barStyle = useAppSelector(state => state.theme.barStyle);
    const backgroundColor = useAppSelector(state => state.theme.backgroundColor);
    return (
        <StatusBarOrigin 
            barStyle={barStyle} 
            backgroundColor={"transparent"}
            translucent
            {...props} />
    )
}

const style = StyleSheet.create({
    nav: {
        backgroundColor: "dark",
        flexDirection: "row",
    }
})

export function DesktopStatusBar(props: ComponentProps<typeof StatusBarOrigin>) {
    return null
}

export const StatusBar = Device.isMobile ? MobileStatusBar : DesktopStatusBar;