import { EmbyConfig, config } from '@api/config';
import { Api, Emby } from '@api/emby';
import { login } from '@api/login';
import { Navigation } from '@global';
import { StorageHelper } from '@helper/store';
import { useAppDispatch } from '@hook/store';
import { useNavigation } from '@react-navigation/native';
import { loginToSiteAsync } from '@store/embySlice';
import {useState} from 'react';
import {Button, SafeAreaView, StyleSheet, Text, TextInput, View} from 'react-native';

const style = StyleSheet.create({
    inputLine: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingLeft: 5,
    },
    inputLabel: {
        flexShrink: 0,
        minWidth: 50,
        fontWeight: "600",
        fontSize: 16,
    },
    input: {
        flex: 1,
        height: 40,
        margin: 12,
        fontSize: 16,
        borderWidth: 1,
        borderRadius: 5,
        padding: 2.5,
        backgroundColor: "#f0f0f0",
        borderColor: "#e0e0e0",
    },
    loginButton: {
        marginTop: 12,
        marginBottom: 12,
        marginLeft: "auto",
        marginRight: "auto",
        backgroundColor: "black",
        color: "white",
        borderRadius: 5,
        width: "75%"
    }
});

export function Page() {
    const navigation: Navigation = useNavigation()
    const [server, onChangeServer] = useState('');
    const [username, onChangeUsername] = useState('');
    const [password, onChangePassword] = useState('');
    const [loading, setLoading] = useState(false);
    const dispatch = useAppDispatch()
    const onLoginPress = async () => {
        const regex = /(?<protocol>http|https):\/\/(?<host>[^\/]+):?(?<port>\d+)?(?<path>\/?.*)/
        const groups = server.match(regex)?.groups
        const endpoint: EmbyConfig = {
            host: groups?.host ?? "",
            port: groups?.port ? parseInt(groups.port) : 443,
            protocol: groups?.protocol ?? "https" as any,
            path: groups?.path ? (groups?.path.length === 0 ? "/" : groups?.path) : "/"
        }

        const callback = {
            resolve: () => {
                setLoading(false)
                setTimeout(() => {
                    navigation.navigate("home")
                }, 1000)
                console.log("login success")
            },
            reject: () => {
                setLoading(false)
                console.log("login failed")
            }
        }
        setLoading(true)
        dispatch(loginToSiteAsync({
            endpoint,
            username, 
            password, 
            callback
        }))
    }
    return (
        <SafeAreaView>
            <View style={style.inputLine}>
                <Text style={style.inputLabel}>服务器</Text>
                <TextInput
                    placeholder="https://server.emby.media"
                    style={style.input}
                    onChangeText={onChangeServer}
                    value={server}
                />
            </View>
            <View style={style.inputLine}>
                <Text style={style.inputLabel}>用户名</Text>
                <TextInput
                    placeholder="guest"
                    style={style.input}
                    onChangeText={onChangeUsername}
                    value={username}
                />
            </View>
            <View style={style.inputLine}>
                <Text style={style.inputLabel}>密码</Text>
                <TextInput
                    placeholder="password"
                    style={style.input}
                    onChangeText={onChangePassword}
                    value={password}
                />
            </View>
            <View style={style.loginButton}>
                <Button title="登录" color="white" onPress={onLoginPress} />
            </View>
        </SafeAreaView>
    );
}
