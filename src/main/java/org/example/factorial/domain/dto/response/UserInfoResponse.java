package org.example.factorial.domain.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserInfoResponse {
	private UserResponse userResponse;
	private List<UserRatingHistoryInfoResponse> userRatingHistoryInfoResponseList;
}
