package com.lowlifelove.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDTO 用于在服务层和 Mapper 之间传递用户信息。 包含用户的唯一标识以及角色状态字段。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	/**
	 * 用户唯一标识
	 */
	private Long id;

	/**
	 * 角色标识字段：客户标识 值为 1 表示当前用户为客户，值为 0 表示不是客户
	 */
	private Integer isClient;

	/**
	 * 角色标识字段：作者标识 值为 1 表示当前用户为作者，值为 0 表示不是作者
	 */
	private Integer isAuthor;
}
