import DefaultLayout from "@/layouts/default";
import GuardProvider from "@/components/Commons/Pages/Security/GuardProvider";
import Running from "@/components/Rulling";

export default function Index() {
  return (
    <GuardProvider>
      <DefaultLayout>
        <Running />
      </DefaultLayout>
    </GuardProvider>
  );
}
