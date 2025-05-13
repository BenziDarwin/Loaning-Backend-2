package com.yunesta.mpesa.member

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/members")
class MemberController(private val memberService: MemberService) {

    // Create a new member
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createMember(@RequestBody memberRequest: Member): Member {
        return memberService.createMember(memberRequest)
    }

    // Get all members
    @GetMapping
    fun getAllMembers(): List<Member> {
        return memberService.getAllMembers()
    }

    // Get a member by ID
    @GetMapping("/{id}")
    fun getMemberById(@PathVariable id: String): Member {
        return memberService.getMemberById(id)
    }

    // Update a member by ID
    @PutMapping("/{id}")
    fun updateMember(
        @PathVariable id: String,
        @RequestBody memberRequest: Member
    ): Member {
        return memberService.updateMember(memberRequest, id)
    }

    // Delete a member by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMember(@PathVariable id: String) {
        memberService.deleteMember(id)
    }
}
