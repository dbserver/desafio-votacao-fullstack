import { Box } from "@mui/material";

export default function MainContainer({children, }: {
    children: React.ReactNode;
}) {
    return (
        <Box
            sx={{
                backgroundColor: "white",
                width: "100%",
                height: {
                    minHeight: "100vh"
                },
                marginTop: 4,
                marginBottom: 4,
                // marginRight: 2,
                // paddingY: 2,
                // paddingX: 4,
                // borderRadius: 6,
                // "@media (max-width: 768px)": {
                //     marginRight: 0,
                //     marginTop: 7,
                //     borderRadius: 0,
                // },
            }}
        >
            {children}
        </Box>
    );
}