import { ResponsiveCalendar } from '@nivo/calendar'

const colors = [
    '#9FDFCD',
    '#D79AD7',
    '#FFAAB9',
]

const MyResponsiveCalendar = ({ data }) => (
    <ResponsiveCalendar
        data={data}
        from="2022-01-01"
        to="2022-12-31"
        emptyColor="#eeeeee"
        colors={[ '#61cdbb', '#97e3d5', '#e8c1a0', '#f47560' ]}
        margin={{ top: -200, right: 40, bottom: 0, left: 20 }}
        yearSpacing={40}
        monthBorderColor="#ffffff"
        dayBorderWidth={2}
        dayBorderColor="#ffffff"
        legends={[
            {
                anchor: 'bottom-right',
                direction: 'row',
                translateY: 36,
                translateX:-100,
                itemCount: 4,
                itemWidth: 42,
                itemHeight: 36,
                itemsSpacing: 14,
                itemDirection: 'right-to-left'
            }
        ]}
    />
)

export default MyResponsiveCalendar