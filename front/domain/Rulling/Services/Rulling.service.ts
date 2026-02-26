import TNewRulling from "@/domain/Rulling/Types/TNewRulling";
import ApiClient from "@/commons/clients/ApiClient";
import { Rulling } from "@/domain/Rulling/Model/Rulling";
import TRullingPaginationInfo from "@/domain/Rulling/Types/TRullingPaginationInfo";

export class RullingService {
  public static registerRulling(data: TNewRulling) {
    return ApiClient().post<Rulling>("/rulling", data);
  }

  public static getPaginatedRullingsInfo() {
    return ApiClient().get<TRullingPaginationInfo>("/rulling/navigate/info");
  }

  public static getRullingsByPage(page: number) {
    return ApiClient().get<Rulling[]>(`/rulling/all/${page}`);
  }

  public static voteRullingYes(rullingId: string) {
    return ApiClient().patch(`/rulling/${rullingId}/vote/yes`);
  }

  public static voteRullingNo(rullingId: string) {
    return ApiClient().patch(`/rulling/${rullingId}/vote/no`);
  }
}
