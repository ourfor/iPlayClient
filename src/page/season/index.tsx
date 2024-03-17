import { PropsWithNavigation } from "@global";
import { useAppSelector } from "@hook/store";
import { useEffect, useState } from "react";
import { Image, SafeAreaView, ScrollView, StatusBar, StyleSheet, Text, View } from "react-native";
import { Season } from "@model/Season";
import { Episode } from "@model/Episode";
import { EpisodeCard } from "@view/EpisodeCard";

const style = StyleSheet.create({
    page: {
        flex: 1,
    }
});

export type SeasonPageProps = PropsWithNavigation<'season'>;
export function Page({route}: SeasonPageProps) {
    const season: Season = route.params.season
    const emby = useAppSelector(state => state.emby?.emby)
    const [episodes, setEpisodes] = useState<Episode[]>([])
    useEffect(() => {
        emby?.getEpisodes?.(Number(season.SeriesId), Number(season.Id))
        .then(setEpisodes);
    }, [emby, season])
    return (
        <SafeAreaView style={style.page}>
            <StatusBar />
            <ScrollView
                contentInsetAdjustmentBehavior="automatic"
                showsHorizontalScrollIndicator={false}
                showsVerticalScrollIndicator={false}
                style={{flex: 1}}>
                <View>
                    <Image source={{uri: emby?.imageUrl?.(season.ImageTags.Primary, null)}} />
                    <Text>{season.Name}</Text>
                </View>
                {episodes?.map(episode => 
                    <EpisodeCard key={episode.Id} emby={emby} episode={episode} />
                )}
            </ScrollView>
        </SafeAreaView>
    )
}