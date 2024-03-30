import { ViewStyle } from "react-native";
import { DataProvider, Dimension, LayoutProvider, RecyclerListView } from "recyclerlistview";

export const ListBaseView = RecyclerListView

export interface MyListViewProps<T> {
    items: T[]
    render: (item: T, index: number, type: number | string) => JSX.Element
    style?: ViewStyle;
    typeForIndex?: (index: number) => number | string;
    layoutForType?: (type: string | number, dim: Dimension, index: number) => void
    isHorizontal?: boolean
    showsHorizontalScrollIndicator?: boolean
    showsVerticalScrollIndicator?: boolean
    isIdentity?: (a: T, b: T) => boolean
}

export function ListView<T>({
    items,
    render,
    style,
    typeForIndex = (idx) => 0,
    layoutForType = (type, dim, index) => {
        dim.width = 120
        dim.height = 120
    },
    isHorizontal = false,
    showsHorizontalScrollIndicator = false,
    showsVerticalScrollIndicator = false,
    isIdentity = (a, b) => a === b
}: MyListViewProps<T>) {
    const dataProvider = new DataProvider(isIdentity).cloneWithRows(items)

    const layoutProvider = new LayoutProvider(
        typeForIndex,
        layoutForType
    )

    return (
        <ListBaseView style={style}
            isHorizontal={isHorizontal}
            showsHorizontalScrollIndicator={showsHorizontalScrollIndicator}
            showsVerticalScrollIndicator={showsVerticalScrollIndicator}
            dataProvider={dataProvider} 
            layoutProvider={layoutProvider} 
            rowRenderer={(type, data, i) => render(data, i, type)} />
    )
}