#!/bin/bash

# Script to remove empty directories after refactoring
# Run this script from the project root directory

echo "Removing empty directories..."

# Remove empty directories
rmdir "app/src/main/java/kontestbuddybyamitmaity/example/kontestbuddy/Services" 2>/dev/null
rmdir "app/src/main/java/kontestbuddybyamitmaity/example/kontestbuddy/Fragment" 2>/dev/null
rmdir "app/src/main/java/kontestbuddybyamitmaity/example/kontestbuddy/Friends" 2>/dev/null
rmdir "app/src/main/java/kontestbuddybyamitmaity/example/kontestbuddy/CurrentFriends" 2>/dev/null
rmdir "app/src/main/java/kontestbuddybyamitmaity/example/kontestbuddy/questionsStats" 2>/dev/null

echo "Cleanup completed!"
echo ""
echo "Remaining structure:"
echo "📁 data/ - Data layer (API, local, models, repository)"
echo "📁 domain/ - Domain layer (use cases)"
echo "📁 presentation/ - Presentation layer (UI, ViewModels)"
echo "📁 utils/ - Utilities"
echo "📁 di/ - Dependency injection"
echo "📁 Auth/ - Authentication (to be moved to presentation/ui/activity/)"
echo "📁 Compare/ - Comparison features (to be moved to presentation/ui/activity/)"
echo "📁 Ranking/ - Ranking features (to be moved to presentation/ui/activity/)"
echo "📁 Notifications/ - Notifications (to be moved to presentation/ui/)"
echo "📄 MainActivity.kt - Main activity (to be moved to presentation/ui/activity/)"
echo "📄 SpalshActivity.kt - Splash activity (to be moved to presentation/ui/activity/)"
