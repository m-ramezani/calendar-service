import {RouterModule, Routes} from "@angular/router";
import {UserRegisterComponent} from "./components/user-register/user-register.component";
import {UserLoginComponent} from "./components/user-login/user-login.component";
import {MainComponent} from "./components/main/main.component";


const routes: Routes = [
  { path: '', component: MainComponent},
  { path: 'register', component: UserRegisterComponent },
  { path: 'login', component: UserLoginComponent },
  { path: '**', redirectTo: '' }
];
export const appRoutingModule = RouterModule.forRoot(routes);
