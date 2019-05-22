/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { InteractionTypeDetailComponent } from 'app/entities/interaction-type/interaction-type-detail.component';
import { InteractionType } from 'app/shared/model/interaction-type.model';

describe('Component Tests', () => {
    describe('InteractionType Management Detail Component', () => {
        let comp: InteractionTypeDetailComponent;
        let fixture: ComponentFixture<InteractionTypeDetailComponent>;
        const route = ({ data: of({ interactionType: new InteractionType(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [InteractionTypeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(InteractionTypeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InteractionTypeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.interactionType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
