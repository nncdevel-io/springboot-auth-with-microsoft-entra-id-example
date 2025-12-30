# Claude Development Rules

This document defines rules and guidelines for Claude AI when working on this project.

## Documentation Rules

### Always Update Related Documentation

**CRITICAL**: When making any code changes, you **MUST** update all related documentation:

- **README.md**: Update when adding features, changing setup instructions, or modifying usage
- **API Documentation**: Update when changing API endpoints or interfaces
- **Configuration Files**: Update comments and examples when changing configuration
- **CHANGELOG.md**: Document all notable changes (if exists)
- **Code Comments**: Update inline documentation when modifying code logic

### Documentation Update Checklist

Before completing any task, verify:

1. ✅ README.md reflects all new features or changes
2. ✅ Setup instructions are current and accurate
3. ✅ Code examples in documentation still work
4. ✅ Version numbers are updated (if applicable)
5. ✅ Configuration examples match actual code

### When Documentation Updates Are Required

- Adding new features → Update README.md with usage examples
- Changing dependencies → Update installation/setup sections
- Modifying APIs → Update API documentation
- Adding configuration options → Document in README.md and config files
- Fixing bugs → Update troubleshooting sections if relevant
- Changing project structure → Update architecture documentation

## Git Workflow Rules

### Commits

- Write clear, descriptive commit messages
- Use conventional commit format when possible (e.g., `feat:`, `fix:`, `docs:`)
- Commit related changes together
- Always include documentation updates in the same commit as code changes

### Branches

- Work on the designated feature branch
- Never push to main/master without explicit permission
- Branch names should be descriptive of the work being done

## Code Quality Rules

### Before Completing Any Task

1. Verify all tests pass
2. Check that documentation is updated
3. Review changes for security vulnerabilities
4. Ensure code follows project conventions
5. Confirm all related files are updated

## Failure Recovery

If you realize documentation was not updated:

1. Immediately update the missing documentation
2. Commit the documentation updates
3. Ensure this doesn't happen again by following this checklist

---

**Remember**: Documentation is not optional. It is a critical part of every code change.
